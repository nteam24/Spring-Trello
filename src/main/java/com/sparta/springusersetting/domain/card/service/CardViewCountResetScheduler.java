package com.sparta.springusersetting.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CardViewCountResetScheduler {

    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCardViewCounts() {
        System.out.println("카드 조회수 초기화");
        // cardViewCount 로 시작하는 데이터 초기화
        redisTemplate.delete(redisTemplate.keys("cardViewCount:*"));
    }
}
