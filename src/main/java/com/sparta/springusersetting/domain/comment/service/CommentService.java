package com.sparta.springusersetting.domain.comment.service;

import com.sparta.springusersetting.domain.board.exception.UnauthorizedActionException;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.comment.exception.NotFoundCommentException;
import com.sparta.springusersetting.domain.comment.exception.UnauthorizedCommentAccessException;
import com.sparta.springusersetting.domain.comment.repository.CommentRepository;
import com.sparta.springusersetting.domain.participation.service.MemberManageService;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.user.service.UserService;
import com.sparta.springusersetting.domain.webhook.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardService cardService;
    private final UserService userService;
    private final WebhookService webhookService;
    private final MemberManageService memberManageService;

    // 댓글 등록
    public CommentResponseDto createComment(long userId, Long cardId, CommentRequestDto requestDto) {
        // 유저 조회
        User user = userService.findUser(userId);
        // 카드 조회
        Card card = cardService.findCard(cardId);
        
        // 멤버 역할 확인 ( 읽기 전용 멤버 댓글 등록 제한 )
        MemberRole memberRole = memberManageService.checkMemberRole(user.getId(), card.getLists().getBoard().getWorkspace().getId());
        if (memberRole.equals(MemberRole.ROLE_READ_USER)) {
            throw new UnauthorizedActionException();
        }

        // Entity 저장
        Comment comment = new Comment(user, card, requestDto);

        //DB 저장
        commentRepository.save(comment);

        // 디스코드 알림 전송
        webhookService.sendDiscordNotification(
                "%s 님이 <%s> 카드에 댓글을 달았어요 ! [ %s (%s) ]",
                user.getUserName(),
                card.getTitle(),
                requestDto.getContent(),
                requestDto.getCommentEmoji()
        );

        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    public CommentResponseDto updateComment(long userId, Long commentId, CommentRequestDto requestDto) {
        // 유저 조회
        User user = userService.findUser(userId);

        // 댓글 조회
        Comment comment = findComment(commentId);

        // 댓글 작성자 일치 확인
        matchCommentWriter(user.getId(), comment.getUser().getId());

        // 댓글 수정
        comment.update(requestDto);

        // DB 저장
        commentRepository.save(comment);

        return new CommentResponseDto(comment);
    }

    // 댓글 삭제
    public String deleteComment(long userId, Long commentId) {
        // 유저 조회
        User user = userService.findUser(userId);

        // 댓글 조회
        Comment comment = findComment(commentId);

        // 댓글 작성자 일치 확인
        matchCommentWriter(user.getId(), comment.getUser().getId());

        commentRepository.delete(comment);

        return "댓글이 삭제되었습니다.";
    }


    // 댓글 조회 메서드
    public Comment findComment (long commentId) {
        return commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
    }

    // 댓글 작성자 일치 확인 메서드
    public void matchCommentWriter(long signinUserId, long commentUserId) {
        if (signinUserId != commentUserId) {
            throw new UnauthorizedCommentAccessException();
        }
    }
}