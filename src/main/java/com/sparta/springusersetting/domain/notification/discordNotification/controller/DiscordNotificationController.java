package com.sparta.springusersetting.domain.notification.discordNotification.controller;

import com.sparta.springusersetting.domain.notification.discordNotification.service.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DiscordNotificationController {

    private final DiscordNotificationService discordNotificationService;

    @GetMapping("/send-discord")
    public String sendDiscordMessage(@RequestParam String message) {
        discordNotificationService.sendDiscordNotification(message);
        return "알림이 전송되었습니다!";
    }
}