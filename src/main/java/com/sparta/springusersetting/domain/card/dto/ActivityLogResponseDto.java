package com.sparta.springusersetting.domain.card.dto;

import com.sparta.springusersetting.domain.card.entity.ActivityLog;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ActivityLogResponseDto {

    private Long id;
    private Long cardId;
    private String contents;
    private LocalDateTime updatedAt;
    private String updatedBy;


    public ActivityLogResponseDto(ActivityLog activityLog)
    {
        this.id = activityLog.getId();
        this.cardId = activityLog.getCard().getId();
        this.contents = activityLog.getContents();
        this.updatedAt = activityLog.getUpdatedAt();
        this.updatedBy = activityLog.getUpdatedBy();
    }


}
