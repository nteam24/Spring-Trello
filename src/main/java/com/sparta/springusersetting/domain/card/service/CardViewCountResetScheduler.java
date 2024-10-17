package com.sparta.springusersetting.domain.card.service;

import com.sparta.springusersetting.domain.card.entity.Card;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class CardViewCountResetScheduler {

    private final RedisTemplate<String, Object> redisTemplate;

    private final CardService cardService;

    // 자정마다 일일 랭킹, 일일 조회수 초기화
    @Scheduled(cron = "0 0 0 * * ?")
    public void resetCardViewCounts() {
        System.out.println("카드 조회수 초기화");

        // 초기화 전에 쌓인 조회수 털기
        resetSnapshotKeys();
        // "todayCardViewSet, todayCardRanking 초기화"
        Set<String> keysToDelete = redisTemplate.keys("todayCardViewSet:*");
        keysToDelete.addAll(redisTemplate.keys("todayCardRanking"));

        if (!keysToDelete.isEmpty()) {
            redisTemplate.delete(keysToDelete);
        }
    }

    // 9시부터 21시까지 1시간 마다 snapshot 초기화
    @Scheduled(cron = "0 0  9-21 * * ?")
    public void resetSnapshotKeys() {
        System.out.println("card:snapshot 키 초기화");

        // "card:snapshot:*" 모든 키 조회
        Set<String> keysToDelete = redisTemplate.keys("card:snapshot(1h):*");

        if (keysToDelete != null && !keysToDelete.isEmpty()) {
            for (String key : keysToDelete) {
                // key: "card:snapshot:{cardId}"에서 cardId 추출
                Long cardId = extractCardIdFromKey(key);

                // Redis 에서 해당 key 의 조회수 값 가져오기
                Integer snapshotViewCount = (Integer) redisTemplate.opsForValue().get(key);

                if (snapshotViewCount != null) {
                    // Card 조회
                    Card card = cardService.findCard(cardId);

                    // totalCardViewCount 에 snapshot 조회수를 더함
                    card.updateTotalCardViewCount(card.getTotalCardViewCount() + snapshotViewCount);

                    // Card 객체 저장
                    cardService.saveCard(card);
                }
            }
            CardService.previousViewCount = 0L;

            // Redis 에서 모든 snapshot 키 삭제
            redisTemplate.delete(keysToDelete);
            System.out.println(keysToDelete.size() + "개의 snapshot 키가 삭제되었습니다.");
        }
    }

    // "card:snapshot:{cardId}"에서 cardId 추출 메서드
    private Long extractCardIdFromKey (String key ){
        return Long.valueOf(key.split(":")[2]);
    }
}
