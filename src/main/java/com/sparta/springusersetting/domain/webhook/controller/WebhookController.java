package com.sparta.springusersetting.domain.webhook.controller;

import com.sparta.springusersetting.domain.webhook.service.WebhookService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookController {

    private final WebhookService webhookService;

    public WebhookController(WebhookService webhookService) {
        this.webhookService = webhookService;
    }

    @GetMapping("/send-discord")
    public String sendDiscordMessage(@RequestParam String message) {
        webhookService.sendDiscordNotification(message);
        return "알림이 전송되었습니다!";
    }
}