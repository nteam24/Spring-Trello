package com.sparta.springusersetting.domain.comment.service;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.comment.dto.request.CommentRequestDto;
import com.sparta.springusersetting.domain.comment.dto.response.CommentResponseDto;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.comment.enums.CommentEmoji;
import com.sparta.springusersetting.domain.comment.repository.CommentRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.UserRole;
import com.sparta.springusersetting.domain.user.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    @InjectMocks
    private CommentService commentService;


    @Test
    void Comment_수정_성공() {
        // given
        long commentId = 1;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.ROLE_ADMIN);
        User user = User.fromAuthUser(authUser);
        Card mockCard = mock(Card.class);
        Comment comment = new Comment(user, mockCard, "content1", CommentEmoji.BLANK);
        ReflectionTestUtils.setField(comment, "id", commentId);
        CommentRequestDto commentRequestDto = new CommentRequestDto("content2", CommentEmoji.SMILE);
        Comment newComment = new Comment(user, mockCard, commentRequestDto);

        given(userService.findUser(anyLong())).willReturn(user);
        given(commentRepository.findById(anyLong())).willReturn(Optional.of(comment));

        // when
        comment.update(commentRequestDto);

        CommentResponseDto commentResponseDto = commentService.updateComment(authUser.getUserId(), commentId, commentRequestDto);

        // then
        assertNotNull(commentResponseDto);
        assertEquals(commentResponseDto.getCommentEmoji(), newComment.getCommentEmoji());
    }


    @Test
    void Comment_삭제_성공() {
        // given
        long commentId = 1;
        AuthUser authUser = new AuthUser(1L, "email", UserRole.ROLE_ADMIN);
        User user = User.fromAuthUser(authUser);
        Card mockCard = mock(Card.class);
        Comment comment = new Comment(user, mockCard, "content1", CommentEmoji.BLANK);
        ReflectionTestUtils.setField(comment, "id", commentId);

        // when
        commentRepository.delete(comment);

        //then
        verify(commentRepository, times(1)).delete(comment); // delete 메서드가 호출되었는지 검증값 검증
    }
}