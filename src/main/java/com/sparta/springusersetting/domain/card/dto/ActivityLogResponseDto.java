package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.card.entity.ActivityLog;
import com.sparta.springusersetting.domain.card.entity.Card;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityLogResponseDto {
    private Long id;
    private String content;
    private LocalDateTime updateAt;
    private String updateBy;

    public ActivityLogResponseDto(ActivityLog activityLog){
        this.id = activityLog.getId();
        this.content = activityLog.getContents();
        this.updateAt = activityLog.getUpdatedAt();
        this.updateBy = activityLog.getUpdatedBy();
    }
}
