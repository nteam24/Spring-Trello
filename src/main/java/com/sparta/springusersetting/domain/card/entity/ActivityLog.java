package com.sparta.springusersetting.domain.card.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class ActivityLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cardId;
    private String contents;
    private LocalDateTime updatedAt;
    private String updatedBy;


    public ActivityLog(Long cardId, String contents, LocalDateTime updatedAt, String updatedBy)
    {
        this.cardId = cardId;
        this.contents = contents;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
