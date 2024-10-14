package com.sparta.springusersetting.domain.comment.service;

import com.sparta.springusersetting.domain.card.repository.CardRepository;
import com.sparta.springusersetting.domain.card.service.CardService;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final CardService cardService;

    public CommentResponseDto createComment(long userId, Long cardId, CommentRequestDto requestDto) {
        return null;
    }
}
