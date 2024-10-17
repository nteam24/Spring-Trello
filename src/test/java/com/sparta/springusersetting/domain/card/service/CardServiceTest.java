package com.sparta.springusersetting.domain.card.service;

import com.sparta.springusersetting.domain.attachment.service.AttachmentService;
import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.card.dto.CardRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import com.sparta.springusersetting.domain.card.dto.CardWithViewCountResponseDto;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.exception.BadAccessCardException;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.common.exception.GlobalExceptionConst;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.repository.ListsRepository;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.exception.BadAccessUserException;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {
    @Mock
    private CardRepository cardRepository;
    @Mock
    private ListsRepository listsRepository;
    @Mock
    private UserService userService;
    @Mock
    private MemberManageService memberManageService;
    @Mock
    private RedisTemplate<String, Object> redisTemplate;
    @Mock
    private SetOperations<String, Object> setOperations;
    @Mock
    private ZSetOperations<String, Object> zSetOperations;
    @Mock
    private ValueOperations<String, Object> valueOperations;
    @Mock
    private AttachmentService attachmentService;
    @InjectMocks
    private CardService cardService;

    private AuthUser authUser;
    private User user;
    private Workspace workspace;
    private Board board;
    private Lists lists;
    private Card card;
    private CardRequestDto cardRequestDto;

    @BeforeEach
    void setUp() {
        authUser = new AuthUser(1L, "test@example.com", UserRole.ROLE_USER);
        user = User.fromAuthUser(authUser);
        workspace = new Workspace("Test Workspace", "Test Description");
        ReflectionTestUtils.setField(workspace, "id", 1L);
        board = new Board("Test Board", "#FFFFFF", null, workspace);
        ReflectionTestUtils.setField(board, "id", 1L);
        lists = new Lists("Test List", 1, user.getEmail(), board);
        ReflectionTestUtils.setField(lists, "id", 1L);
        card = new Card(user, lists, "Test Card", "Test Content", LocalDate.now());
        ReflectionTestUtils.setField(card, "id", 1L);

        cardRequestDto = new CardRequestDto();
        ReflectionTestUtils.setField(cardRequestDto, "managerId", 1L);
        ReflectionTestUtils.setField(cardRequestDto, "listId", 1L);
        ReflectionTestUtils.setField(cardRequestDto, "title", "Test Card");
        ReflectionTestUtils.setField(cardRequestDto, "contents", "Test Content");
        ReflectionTestUtils.setField(cardRequestDto, "deadline", LocalDate.now());
    }

    @Nested
    @DisplayName("카드 생성 테스트")
    class createCardTest{
        @Test
        void createCard_성공() throws IOException {
            // given
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file content".getBytes());

            given(listsRepository.findById(anyLong())).willReturn(Optional.of(lists));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(userService.findUser(anyLong())).willReturn(user);
            given(cardRepository.save(any(Card.class))).willAnswer(invocation -> {
                Card savedCard = invocation.getArgument(0);
                ReflectionTestUtils.setField(savedCard, "id", 1L);
                return savedCard;
            });

            // when
            String result = cardService.createCard(authUser, cardRequestDto, file);

            // then
            assertEquals("카드 생성이 완료되었습니다.", result);
            verify(cardRepository, times(1)).save(any(Card.class));
            verify(attachmentService, times(1)).saveFile(eq(authUser), eq(1L), eq(file));
        }

        @Test
        void createCard_실패_권한없음() throws IOException {
            // given
            given(listsRepository.findById(anyLong())).willReturn(Optional.of(lists));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            assertThrows(BadAccessUserException.class, () ->
                    cardService.createCard(authUser, cardRequestDto, null)
            );
            verify(cardRepository, never()).save(any(Card.class));
            verify(attachmentService, never()).saveFile(any(), anyLong(), any());
        }

        @Test
        void createCard_성공_파일없음() throws IOException {
            // given
            given(listsRepository.findById(anyLong())).willReturn(Optional.of(lists));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(userService.findUser(anyLong())).willReturn(user);
            given(cardRepository.save(any(Card.class))).willAnswer(invocation -> {
                Card savedCard = invocation.getArgument(0);
                ReflectionTestUtils.setField(savedCard, "id", 1L);
                return savedCard;
            });

            // when
            String result = cardService.createCard(authUser, cardRequestDto, null);

            // then
            assertEquals("카드 생성이 완료되었습니다.", result);
            verify(cardRepository, times(1)).save(any(Card.class));
            verify(attachmentService, never()).saveFile(any(), anyLong(), any());
        }

        @Test
        void createCard_실패_파일저장오류() throws IOException {
            // given
            MockMultipartFile file = new MockMultipartFile("file", "test.txt", "text/plain", "test file content".getBytes());

            given(listsRepository.findById(anyLong())).willReturn(Optional.of(lists));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(userService.findUser(anyLong())).willReturn(user);
            given(cardRepository.save(any(Card.class))).willAnswer(invocation -> {
                Card savedCard = invocation.getArgument(0);
                ReflectionTestUtils.setField(savedCard, "id", 1L);
                return savedCard;
            });
            doThrow(new IOException("File save error")).when(attachmentService).saveFile(any(), anyLong(), any());

            // when & then
            assertThrows(RuntimeException.class, () -> cardService.createCard(authUser, cardRequestDto, file));
            verify(cardRepository, times(1)).save(any(Card.class));
            verify(attachmentService, times(1)).saveFile(eq(authUser), eq(1L), eq(file));
        }
    }

    @Nested
    @DisplayName("카드 조회 테스트")
    class getCardTest{
        @Test
        void getCard_성공() {
            // given
            Long cardId = 1L;
            String cardViewSetKey = "cardViewSet:" + cardId;
            String rankingKey = "cardRanking";
            String cardTitleKey = "cardTitle:" + cardId;

            given(redisTemplate.opsForSet()).willReturn(setOperations);
            given(redisTemplate.opsForZSet()).willReturn(zSetOperations);
            given(redisTemplate.opsForValue()).willReturn(valueOperations);

            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            given(setOperations.add(eq(cardViewSetKey), anyString())).willReturn(1L);
            given(setOperations.size(cardViewSetKey)).willReturn(5L);

            Set<Object> topCards = new HashSet<>(Arrays.asList("Card1", "Card2", "Card3"));
            given(zSetOperations.reverseRange(rankingKey, 0, 2)).willReturn(topCards);

            given(valueOperations.get(cardTitleKey)).willReturn(null); // Simulating cache miss

            // when
            CardWithViewCountResponseDto result = cardService.getCard(authUser, cardId);

            // then
            assertNotNull(result);
            assertEquals(card.getId(), result.getId());
            assertEquals(card.getTitle(), result.getTitle());
            assertEquals(card.getContents(), result.getContents());
            assertEquals(card.getDeadline(), result.getDeadline());
            assertEquals(user.getEmail(), result.getUserEmail());
            assertEquals(5L, result.getCardViewCount());
            assertTrue(result.getActivityLogs().isEmpty());
            assertTrue(result.getCommentList().isEmpty());

            verify(userService).findUser(authUser.getUserId());
            verify(cardRepository, times(2)).findById(cardId);
            verify(setOperations).add(eq(cardViewSetKey), eq(user.getId().toString()));
            verify(setOperations, times(2)).size(cardViewSetKey);
            verify(zSetOperations).add(eq(rankingKey), eq(card.getTitle()), anyDouble());
            verify(zSetOperations).reverseRange(rankingKey, 0, 2);
            verify(valueOperations).get(cardTitleKey);
            verify(valueOperations).set(eq(cardTitleKey), eq(card.getTitle()));
        }

        @Test
        void getCard_카드없음() {
            // given
            Long nonExistentCardId = 999L;

            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(cardRepository.findById(nonExistentCardId)).willReturn(Optional.empty());

            // when & then
            assertThrows(RuntimeException.class, () -> cardService.getCard(authUser, nonExistentCardId));

            // Verify
            verify(userService).findUser(authUser.getUserId());
            verify(cardRepository).findById(nonExistentCardId);
            verifyNoInteractions(redisTemplate);
        }
    }

    @Nested
    @DisplayName("카드 삭제 테스트")
    class deleteCardTest{
        @Test
        void deleteCard_성공() {
            // given
            Long cardId = 1L;
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);

            // when
            String result = cardService.deleteCard(authUser, cardId);

            // then
            assertEquals("카드 삭제가 완료되었습니다.", result);
            verify(cardRepository).findById(cardId);
            verify(memberManageService).checkMemberRole(eq(user.getId()), eq(workspace.getId()));
            verify(cardRepository).delete(card);
        }

        @Test
        void deleteCard_실패_권한없음() {
            // given
            Long cardId = 1L;
            given(cardRepository.findById(cardId)).willReturn(Optional.of(card));
            given(memberManageService.checkMemberRole(anyLong(), anyLong())).willReturn(MemberRole.ROLE_READ_USER);

            // when & then
            BadAccessUserException exception = assertThrows(BadAccessUserException.class, () -> cardService.deleteCard(authUser, cardId));
            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.INVALID_ROLE.getMessage()));
            verify(cardRepository).findById(cardId);
            verify(memberManageService).checkMemberRole(eq(user.getId()), eq(workspace.getId()));
            verify(cardRepository, never()).delete(any(Card.class));
        }

        @Test
        void deleteCard_실패_카드없음() {
            // given
            Long nonExistentCardId = 999L;
            given(cardRepository.findById(nonExistentCardId)).willReturn(Optional.empty());

            // when & then
            assertThrows(NullPointerException.class, () -> cardService.deleteCard(authUser, nonExistentCardId));
            verify(cardRepository).findById(nonExistentCardId);
            verify(memberManageService, never()).checkMemberRole(anyLong(), anyLong());
            verify(cardRepository, never()).delete(any(Card.class));
        }
    }

    @Nested
    @DisplayName("카드 검색 테스트")
    class searchCardTest{
        @Test
        void searchCard_성공() {
            // given
            Long cursorId = 0L;
            int pageSize = 10;
            CardSearchRequestDto searchRequest = new CardSearchRequestDto();
            searchRequest.setWorkspaceId(1L);
            searchRequest.setBoardId(2L);
            searchRequest.setKeyword("Test");
            searchRequest.setDeadline(LocalDate.now().plusDays(7));
            searchRequest.setManagerEmail("manager@example.com");
            LocalDateTime now = LocalDateTime.now();
            List<CardSearchResponseDto> cardList = Arrays.asList(
                    new CardSearchResponseDto(1L, "Card 1", "Content 1", LocalDate.now().plusDays(5), "user1@example.com", now.minusDays(1)),
                    new CardSearchResponseDto(2L, "Card 2", "Content 2", LocalDate.now().plusDays(10), "user2@example.com", now)
            );
            Slice<CardSearchResponseDto> expectedSlice = new SliceImpl<>(cardList);

            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(memberManageService.checkMemberRole(authUser.getUserId(), searchRequest.getWorkspaceId())).willReturn(MemberRole.ROLE_WORKSPACE_ADMIN);
            given(cardRepository.searchCards(searchRequest, cursorId, pageSize)).willReturn(expectedSlice);

            // when
            Slice<CardSearchResponseDto> result = cardService.searchCard(authUser.getUserId(), searchRequest, cursorId, pageSize);

            // then
            assertNotNull(result);
            assertEquals(2, result.getContent().size());

            CardSearchResponseDto firstCard = result.getContent().get(0);
            assertEquals(1L, firstCard.getId());
            assertEquals("Card 1", firstCard.getTitle());
            assertEquals("Content 1", firstCard.getContents());
            assertEquals(LocalDate.now().plusDays(5), firstCard.getDeadline());
            assertEquals("user1@example.com", firstCard.getUserEmail());
            assertEquals(now.minusDays(1), firstCard.getCreatedAt());

            CardSearchResponseDto secondCard = result.getContent().get(1);
            assertEquals(2L, secondCard.getId());
            assertEquals("Card 2", secondCard.getTitle());
            assertEquals("Content 2", secondCard.getContents());
            assertEquals(LocalDate.now().plusDays(10), secondCard.getDeadline());
            assertEquals("user2@example.com", secondCard.getUserEmail());
            assertEquals(now, secondCard.getCreatedAt());

            verify(userService).findUser(authUser.getUserId());
            verify(memberManageService).checkMemberRole(authUser.getUserId(), searchRequest.getWorkspaceId());
            verify(cardRepository).searchCards(searchRequest, cursorId, pageSize);
        }

        @Test
        void searchCard_실패_워크스페이스ID없음() {
            // given
            CardSearchRequestDto searchRequest = new CardSearchRequestDto();
            searchRequest.setWorkspaceId(null);

            // when & then
            BadAccessCardException exception = assertThrows(BadAccessCardException.class, () ->
                    cardService.searchCard(authUser.getUserId(), searchRequest, 0L, 10)
            );
            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.WORKSPACE_ID_REQUIRED.getMessage()));
        }

        @Test
        void searchCard_실패_권한없음() {
            // given
            CardSearchRequestDto searchRequest = new CardSearchRequestDto();
            searchRequest.setWorkspaceId(1L);
            searchRequest.setBoardId(2L);
            searchRequest.setKeyword("Test");
            searchRequest.setDeadline(LocalDate.now().plusDays(7));
            searchRequest.setManagerEmail("manager@example.com");

            given(userService.findUser(authUser.getUserId())).willReturn(user);
            given(memberManageService.checkMemberRole(authUser.getUserId(), searchRequest.getWorkspaceId())).willReturn(null);

            // when & then
            BadAccessCardException exception = assertThrows(BadAccessCardException.class, () ->
                    cardService.searchCard(authUser.getUserId(), searchRequest, 0L, 10)
            );
            assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
            assertTrue(exception.getMessage().contains(GlobalExceptionConst.WORKSPACE_ID_REQUIRED.getMessage()));
        }
    }
}