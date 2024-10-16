package com.sparta.springusersetting.domain.notification.slackNotification;


import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(value = SystemProperties.class)
public class SlackConfig {
}
