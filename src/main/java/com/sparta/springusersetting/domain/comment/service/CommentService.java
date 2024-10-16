package com.sparta.springusersetting.domain.comment.service;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.comment.exception.NotFoundCommentException;
import com.sparta.springusersetting.domain.comment.exception.UnauthorizedCommentAccessException;
import com.sparta.springusersetting.domain.comment.repository.CommentRepository;
import com.sparta.springusersetting.domain.notification.slack.SlackChatUtil;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardService cardService;
    private final UserService userService;
    private final CardRepository cardRepository;
    private final SlackChatUtil slackChatUtil;

    // 댓글 등록
    public CommentResponseDto createComment(long userId, Long cardId, CommentRequestDto requestDto) throws IOException {
        // 유저 조회
        User user = userService.findUser(userId);
        // 카드 조회
        Card card = cardRepository.findById(cardId).orElseThrow();
        
        // 멤버 역할 확인
        
        // Entity 저장
        Comment comment = new Comment(user, card, requestDto);

        slackChatUtil.sendSlackErr("%s 님이 댓글 등록을 하였습니다."+ user.getUserName());

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


    // 유저 조회 메서드
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

/*
    1. cardRepository 로 직접 가져오는 로직 수정
    2. 멤버역할로 댓글 등록 제한
 */