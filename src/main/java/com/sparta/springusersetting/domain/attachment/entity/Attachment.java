package com.sparta.springusersetting.domain.attachment.entity;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Attachment extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String s3Url;


    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Attachment(String s3Url, Card card) {
        this.s3Url = s3Url;
        this.card = card;
    }
    public void saveImage(String s3Url) {
        this.s3Url = s3Url;
    }

}
