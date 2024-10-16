package com.sparta.springusersetting.domain.card.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CardViewCountResetScheduler {

    private final RedisTemplate<String, Object> redisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCardViewCounts() {
        System.out.println("카드 조회수 초기화");

        // 두 패턴의 키를 한 번에 조회하고 삭제
        Set<String> keysToDelete = redisTemplate.keys("cardViewSet:*");
        keysToDelete.addAll(redisTemplate.keys("cardRanking"));

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }
    }

}
