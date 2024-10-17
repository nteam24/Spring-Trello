package com.sparta.springusersetting.domain.lists.service;

import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.board.exception.NotFoundBoardException;
import com.sparta.springusersetting.domain.board.repository.BoardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;
import com.sparta.springusersetting.domain.lists.dto.request.ListsRequestDto;
import com.sparta.springusersetting.domain.lists.dto.response.GetListsResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsCreateResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsResponseDto;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.exception.BadAccessListsException;
import com.sparta.springusersetting.domain.lists.exception.NotFoundListsException;
import com.sparta.springusersetting.domain.lists.repository.ListsRepository;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListsServiceTest {
    @Mock
    private UserService userService;
    @Mock
    private ListsRepository listsRepository;
    @Mock
    private BoardRepository boardRepository;
    @Mock
    private MemberManageService memberManageService;
    @InjectMocks
    private ListsService listsService;

    private AuthUser authUser;
    private User user;
    private Board board;
    private Lists lists;
    private Workspace workspace;
    private Participation participation;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "test@example.com", UserRole.ROLE_USER);
        user = User.fromAuthUser(authUser);
        workspace = new Workspace("Test Workspace", "Test Description");
        ReflectionTestUtils.setField(workspace, "id", 1L);

        participation = new Participation(user, workspace);
        participation.changeRole(MemberRole.ROLE_WORKSPACE_ADMIN);
        participation.UserBeActive();

        board = new Board("Test Board", "#FFFFFF", null, workspace);
        ReflectionTestUtils.setField(board, "id", 1L);

        lists = new Lists("Test List", 1, user.getEmail(), board);
        ReflectionTestUtils.setField(lists, "id", 1L);
    }

    @Nested
    @DisplayName("리스트 생성 테스트")
    class createListsTest{
        @Test
        void Lists_생성_성공(){
            // given
            ListsRequestDto requestDto = new ListsRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "Test List");

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findMaxPositionByBoard(any(Board.class))).willReturn(Optional.of(0));
            given(listsRepository.save(any(Lists.class))).willReturn(lists);

            // when
            ListsCreateResponseDto responseDto = listsService.createLists(1L, requestDto, authUser.getUserId());

            // then
            assertNotNull(responseDto);
            assertEquals("Test List", requestDto.getTitle());
            verify(listsRepository, times(1)).save(any(Lists.class));
        }
        @Test
        void Lists_생성_실패_권한없음(){
            // given
            ListsRequestDto requestDto = new ListsRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "Test List");

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            BadAccessListsException exception = assertThrows(BadAccessListsException.class, () ->
                    listsService.createLists(1L, requestDto, authUser.getUserId()));
            assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.UNAUTHORIZED_LIST_CREATION.getMessage()));
        }
    }

    @Nested
    @DisplayName("리스트 수정 테스트")
    class updateListsTest{
        @Test
        void Lists_수정_성공(){
            // given
            ListsRequestDto requestDto = new ListsRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "Updated List");

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findByIdAndBoardId(anyLong(), anyLong())).willReturn(Optional.of(lists));
            given(listsRepository.save(any(Lists.class))).willReturn(lists);

            // when
            ListsResponseDto responseDto = listsService.updateLists(1L, 1L, requestDto, authUser.getUserId());

            // then
            assertNotNull(responseDto);
            assertEquals("Updated List", responseDto.getTitle());
            verify(listsRepository, times(1)).save(any(Lists.class));
        }

        @Test
        void Lists_수정_실패_권한없음(){
            // given
            ListsRequestDto requestDto = new ListsRequestDto();
            ReflectionTestUtils.setField(requestDto, "title", "Updated List");

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            BadAccessListsException exception = assertThrows(BadAccessListsException.class, () ->
                    listsService.updateLists(1L, 1L, requestDto, authUser.getUserId())
            );

            // Additional assertions
            assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.UNAUTHORIZED_LIST_CREATION.getMessage()));
            verify(listsRepository, never()).save(any(Lists.class));
        }
    }

    @Nested
    @DisplayName("리스트 위치 수정 테스트")
    class updateListsPositionTest{
        @Test
        void Lists_위치_수정_성공(){
            // given
            int newPosition = 3;
            Lists list1 = new Lists("List 1", 1, user.getEmail(), board);
            Lists list2 = new Lists("List 2", 2, user.getEmail(), board);
            Lists list3 = new Lists("List 3", 3, user.getEmail(), board);
            Lists list4 = new Lists("List 4", 4, user.getEmail(), board);

            ReflectionTestUtils.setField(list1, "id", 1L);
            ReflectionTestUtils.setField(list2, "id", 2L);
            ReflectionTestUtils.setField(list3, "id", 3L);
            ReflectionTestUtils.setField(list4, "id", 4L);

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findByIdAndBoardId(anyLong(), anyLong())).willReturn(Optional.of(list2));
            given(listsRepository.findAllByBoardOrderByPos(any(Board.class))).willReturn(Arrays.asList(list1, list2, list3, list4));
            given(listsRepository.save(any(Lists.class))).willAnswer(invocation -> invocation.getArgument(0));

            // when
            ListsResponseDto responseDto = listsService.updateListsPosition(1L, 2L, newPosition, authUser.getUserId());

            // then
            assertNotNull(responseDto);
            assertEquals(newPosition, responseDto.getPos());
            verify(listsRepository, times(4)).save(any(Lists.class));  // 4개의 리스트 모두 저장되어야 함

            // Verify the position of each list
            verify(listsRepository).save(argThat(list -> list.getId().equals(1L) && list.getPos() == 1));
            verify(listsRepository).save(argThat(list -> list.getId().equals(2L) && list.getPos() == 3));
            verify(listsRepository).save(argThat(list -> list.getId().equals(3L) && list.getPos() == 2));
            verify(listsRepository).save(argThat(list -> list.getId().equals(4L) && list.getPos() == 4));
        }
        @Test
        void Lists_위치_수정_실패_권한없음(){
            // given
            int newPosition = 3;

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(userService.findUser(anyLong())).willReturn(user);
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            BadAccessListsException exception = assertThrows(BadAccessListsException.class, () ->
                    listsService.updateListsPosition(1L, 1L, newPosition, authUser.getUserId())
            );

            // Additional assertions
            assertEquals(HttpStatus.FORBIDDEN, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.UNAUTHORIZED_LIST_CREATION.getMessage()));

            // Verify that save was never called
            verify(listsRepository, never()).save(any(Lists.class));
            verify(listsRepository, never()).findByIdAndBoardId(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("리스트 단순 조회 테스트")
    class getListsTest{
        @Test
        void getLists_성공(){
            // given
            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findByIdAndBoardId(anyLong(), anyLong())).willReturn(Optional.of(lists));

            // when
            GetListsResponseDto responseDto = listsService.getLists(1L, 1L, authUser.getUserId());

            // then
            assertNotNull(responseDto);
            assertEquals("Test List", responseDto.getTitle());
            assertEquals(1, responseDto.getPos());
            verify(memberManageService, times(1)).checkMemberRole(anyLong(), anyLong());
            verify(listsRepository, times(1)).findByIdAndBoardId(anyLong(), anyLong());
        }
        @Test
        void getLists_실패_보드없음() {
            // given
            given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundBoardException.class, () ->
                    listsService.getLists(1L, 1L, authUser.getUserId())
            );
            verify(memberManageService, never()).checkMemberRole(anyLong(), anyLong());
            verify(listsRepository, never()).findByIdAndBoardId(anyLong(), anyLong());
        }
    }

    @Nested
    @DisplayName("리스트 삭제 테스트")
    class deleteListsTest{
        @Test
        void deleteLists_성공() {
            // given
            Lists list1 = new Lists("List 1", 1, user.getEmail(), board);
            Lists list3 = new Lists("List 3", 3, user.getEmail(), board);
            ReflectionTestUtils.setField(list1, "id", 2L);
            ReflectionTestUtils.setField(list3, "id", 3L);

            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findByIdAndBoardId(anyLong(), anyLong())).willReturn(Optional.of(lists));
            given(listsRepository.findAllByBoardAndPosGreaterThanOrderByPos(any(Board.class), anyInt()))
                    .willReturn(Arrays.asList(list3));

            // when
            String result = listsService.deleteLists(1L, 1L, authUser.getUserId());

            // then
            assertEquals("삭제가 완료되었습니다.", result);
            verify(listsRepository, times(1)).delete(lists);
            verify(listsRepository, times(1)).save(argThat(list -> list.getPos() == 2 && list.getId().equals(3L)));
        }

        @Test
        void deleteLists_실패_권한없음() {
            // given
            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            assertThrows(BadAccessListsException.class, () ->
                    listsService.deleteLists(1L, 1L, authUser.getUserId())
            );
            verify(listsRepository, never()).delete(any(Lists.class));
            verify(listsRepository, never()).findAllByBoardAndPosGreaterThanOrderByPos(any(Board.class), anyInt());
        }

        @Test
        void deleteLists_실패_보드없음() {
            // given
            given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundBoardException.class, () ->
                    listsService.deleteLists(1L, 1L, authUser.getUserId())
            );
            verify(memberManageService, never()).checkMemberRole(anyLong(), anyLong());
            verify(listsRepository, never()).delete(any(Lists.class));
        }

        @Test
        void deleteLists_실패_리스트없음() {
            // given
            given(boardRepository.findById(anyLong())).willReturn(Optional.of(board));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(listsRepository.findByIdAndBoardId(anyLong(), anyLong())).willReturn(Optional.empty());

            // when & then
            assertThrows(NotFoundListsException.class, () ->
                    listsService.deleteLists(1L, 1L, authUser.getUserId())
            );
            verify(listsRepository, never()).delete(any(Lists.class));
            verify(listsRepository, never()).findAllByBoardAndPosGreaterThanOrderByPos(any(Board.class), anyInt());
        }
    }
}