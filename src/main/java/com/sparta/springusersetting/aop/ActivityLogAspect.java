package com.sparta.springusersetting.aop;


import com.sparta.springusersetting.domain.card.entity.ActivityLog;
import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.card.repository.ActivityLogRepository;
import com.sparta.springusersetting.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;


@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class ActivityLogAspect {

    private ActivityLogRepository activityLogRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @AfterReturning(pointcut = "execution(* com.sparta.springusersetting.domain.card.service.CardService.updateCard(..))", returning = "updateCard")
    public void logCardUpdate(JoinPoint joinPoint, Card updateCard) {
        if (updateCard == null) {
            System.out.println("업데이트된 카드가 없습니다.");
            return;
        }

        Long cardId = updateCard.getId();
        String updatedContent = updateCard.getContents();
        LocalDateTime updatedAt = LocalDateTime.now();

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String updatedBy = (authentication != null && authentication.getPrincipal() instanceof AuthUser)
                ? ((AuthUser) authentication.getPrincipal()).getEmail()
                : "알 수 없는 사용자";

        logger.info("카드 업데이트 로그가 기록되었습니다. ID: {}, 내용: {}, 이메일: {}", cardId, updatedContent, updatedBy);
        ActivityLog activityLog = new ActivityLog(updateCard, updatedContent, updatedAt, updatedBy);
        activityLogRepository.save(activityLog);
    }


}
