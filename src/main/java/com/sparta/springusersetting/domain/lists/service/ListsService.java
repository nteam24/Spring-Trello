package com.sparta.springusersetting.domain.lists.service;

import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.board.exception.NotFoundBoardException;
import com.sparta.springusersetting.domain.board.repository.BoardRepository;
import com.sparta.springusersetting.domain.lists.dto.request.ListsRequestDto;
import com.sparta.springusersetting.domain.lists.dto.response.GetListsResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsCreateResponseDto;
import com.sparta.springusersetting.domain.lists.dto.response.ListsResponseDto;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.lists.exception.BadAccessListsException;
import com.sparta.springusersetting.domain.lists.exception.NotFoundListsException;
import com.sparta.springusersetting.domain.lists.repository.ListsRepository;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.participation.service.WorkspaceManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ListsService {
    private final ListsRepository listsRepository;
    private final BoardRepository boardRepository;
    private final UserService userService;
    private final MemberManageService memberManageService;

    @Transactional
    public ListsCreateResponseDto createLists(Long boardId, ListsRequestDto request, long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());
        User user = userService.findUser(userId);

        // 사용자 권한 확인
        MemberRole memberRole = memberManageService.checkMemberRole(userId, board.getWorkspace().getId());

        if (memberRole == MemberRole.ROLE_READ_USER){
            throw new BadAccessListsException();
        }
        
        // 현재 보드의 마지막 라스트 위치 다음으로 설정
        Integer pos = getLastListsPosition(board) + 1;

        Lists newLists = Lists.builder()
                .title(request.getTitle())
                .pos(pos)
                .createdBy(user.getEmail())
                .board(board)
                .build();

        Lists savedLists = listsRepository.save(newLists);

        return new ListsCreateResponseDto(savedLists);
    }

    @Transactional
    public ListsResponseDto updateLists(Long boardId, Long listsId, ListsRequestDto requestDto, long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userService.findUser(userId);

        // 사용자 권한 확인
        MemberRole memberRole = memberManageService.checkMemberRole(userId, board.getWorkspace().getId());

        if (memberRole == MemberRole.ROLE_READ_USER){
            throw new BadAccessListsException();
        }

        // 리스트 찾기
        Lists lists = listsRepository.findByIdAndBoardId(listsId, boardId)
                .orElseThrow(() -> new NotFoundListsException());

        lists.updateTitle(requestDto.getTitle(), user.getEmail());

        Lists updateLists = listsRepository.save(lists);

        return new ListsResponseDto(updateLists);
    }
    @Transactional
    public ListsResponseDto updateListsPosition(Long boardId, Long listsId, Integer pos, long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        User user = userService.findUser(userId);

        // 사용자 권한 확인
        MemberRole memberRole = memberManageService.checkMemberRole(userId, board.getWorkspace().getId());

        if (memberRole == MemberRole.ROLE_READ_USER){
            throw new BadAccessListsException();
        }

        // 리스트 찾기
        Lists lists = listsRepository.findByIdAndBoardId(listsId, boardId)
                .orElseThrow(() -> new NotFoundListsException());

        lists.updatePosition(pos, user.getEmail());
        
        // 다른 리스트들의 위치 조정
        adjustOtherListsPosition(board, lists, pos);

        Lists updateLists = listsRepository.save(lists);

        return new ListsResponseDto(updateLists);
    }

    @Transactional(readOnly = true)
    public GetListsResponseDto getLists(Long boardId, Long listsId, long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        // 사용자 권한 확인
        memberManageService.checkMemberRole(userId, board.getWorkspace().getId());

        Lists lists = listsRepository.findByIdAndBoardId(listsId, boardId)
                .orElseThrow(() -> new NotFoundListsException());

        return new GetListsResponseDto(lists);
    }

    @Transactional
    public String deleteLists(Long boardId, Long listsId, long userId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new NotFoundBoardException());

        // 사용자 권한 확인
        MemberRole memberRole = memberManageService.checkMemberRole(userId, board.getWorkspace().getId());

        if (memberRole == MemberRole.ROLE_READ_USER){
            throw new BadAccessListsException();
        }

        // 리스트 찾기
        Lists lists = listsRepository.findByIdAndBoardId(listsId, boardId)
                .orElseThrow(() -> new NotFoundListsException());

        listsRepository.delete(lists);

        // 다른 리스트들의 위치 재조정
        adjustListPositionsAfterDeletion(board, lists.getPos());

        return "삭제가 완료되었습니다.";
    }

    private void adjustListPositionsAfterDeletion(Board board, int pos) {
        List<Lists> remainingLists = listsRepository.findAllByBoardAndPosGreaterThanOrderByPos(board, pos);
        for (Lists lists : remainingLists){
            lists.updatePosition(lists.getPos() - 1);
            listsRepository.save(lists);
        }
    }

    private void adjustOtherListsPosition(Board board, Lists movedList, int pos) {
        List<Lists> allLists = listsRepository.findAllByBoardOrderByPos(board);

        // 이동한 리스트를 제외한 나머지 리스트들의 위치를 조정
        int currentPos = 1;
        for (Lists lists : allLists){
            if (lists.getId().equals(movedList.getId())){
                continue;
            }
            if (currentPos == pos){
                currentPos++;
            }
            if (lists.getPos() != currentPos){
                lists.updatePosition(currentPos);
                listsRepository.save(lists);
            }
            currentPos++;
        }
    }

    private Integer getLastListsPosition(Board board) {
        return listsRepository.findMaxPositionByBoard(board)
                .orElse(0);
    }


}
