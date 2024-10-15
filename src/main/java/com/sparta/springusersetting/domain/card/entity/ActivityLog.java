package com.sparta.springusersetting.domain.card.entity;


import jakarta.persistence.*;
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

    private String contents;
    private LocalDateTime updatedAt;
    private String updatedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;


    public ActivityLog(Card card,String contents, LocalDateTime updatedAt, String updatedBy)
    {
        this.card = card;
        this.contents = contents;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }
}
