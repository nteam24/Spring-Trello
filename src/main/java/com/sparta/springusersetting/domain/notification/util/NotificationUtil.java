package com.sparta.springusersetting.domain.notification.util;

//import com.sparta.springusersetting.domain.notification.discordNotification.service.DiscordNotificationService;
import com.sparta.springusersetting.domain.notification.slackNotification.SlackChatUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
@RequiredArgsConstructor
public class NotificationUtil {

    private final SlackChatUtil slackChatUtil;
//    private final DiscordNotificationService discordNotificationService;

    // 알림 전송
    public void sendNotification(String template, Object...args) throws IOException {
        String message = String.format(template, args);
        slackChatUtil.sendSlackErr(message);
//        discordNotificationService.sendDiscordNotification(message);
    }
}
