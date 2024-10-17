package com.sparta.springusersetting.domain.notification.notificationutil;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.notification.slack.SlackChatUtil;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class NotificationUtil {

    private final SlackChatUtil slackChatUtil;

    // 로그인 알람
    public void LoginNotification(User user) throws IOException {
        slackChatUtil.sendSlackErr("난 %s핑! 코딩빼곤 다 재밌어! ",
                user.getUserName()
                );
    }

    // 멤버 추가 알람
    public void AddMemberNotification(User user, Workspace workspace) throws IOException {
        slackChatUtil.sendSlackErr("%s 님이 %s워크스페이스에 참여하였습니다.",
                user.getUserName(),
                workspace.getName()
                );
    }

    // 카드 변경 알림
    public void UpdateCardNotification(User user, Card card) throws IOException {
        slackChatUtil.sendSlackErr(" %s 님이 카드 수정 및 변경을 하였습니다. 제목:%s, 내용:%s , 마감일:%s",
                user.getUserName(),
                card.getTitle(),
                card.getContents(),
                card.getDeadline()
        );
    }

    // 댓글 등록 알림
    public void PostCommentNotification(User user, Card card, Comment comment) throws IOException {
        slackChatUtil.sendSlackErr(" %s 님이 %s 님의 카드에 댓글을 남겼습니다. 내용:%s, %s ",
                    user.getUserName(),
                    card.getManager().getUserName(),
                    comment.getContent(),
                    comment.getCommentEmoji()
                );
    }
}
