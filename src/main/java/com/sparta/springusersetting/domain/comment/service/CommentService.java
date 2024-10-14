package com.sparta.springusersetting.domain.comment.service;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.comment.exception.NotFoundCommentException;
import com.sparta.springusersetting.domain.comment.repository.CommentRepository;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.exception.NotFoundUserException;
import com.sparta.springusersetting.domain.user.repository.UserRepository;
import com.sparta.springusersetting.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardService cardService;
    private final UserService userService;
    private final CardRepository cardRepository;

    // 댓글 등록
    @Transactional
    public CommentResponseDto createComment(long userId, Long cardId, CommentRequestDto requestDto) {
        // 유저 조회
        User user = userService.findUser(userId);
        // 카드 조회
        Card card = cardRepository.findById(cardId).orElseThrow();
        // Entity 저장
        Comment comment = new Comment(user, card, requestDto);

        return new CommentResponseDto(comment);
    }

    // 댓글 수정
    public CommentResponseDto updateComment(long userId, Long cardId, Long commentId, CommentRequestDto requestDto) {
        // 유저 조회
        User user = userService.findUser(userId);
        // 카드 조회
        Card card = cardRepository.findById(cardId).orElseThrow();
        // 댓글 조회
        Comment comment = findComment(commentId);

        // 댓글 작성자 일치 확인
        if (user.getId() != comment.getUser().getId()) {
            throw new IllegalArgumentException("댓글 작성자만 수정이 가능합니다.");
        }

        // 댓글 수정
        comment.update(requestDto);

        return new CommentResponseDto(comment);
    }

    // 유저 조회 메서드
    public Comment findComment (long commentId) {
        return commentRepository.findById(commentId).orElseThrow(NotFoundCommentException::new);
    }
}
