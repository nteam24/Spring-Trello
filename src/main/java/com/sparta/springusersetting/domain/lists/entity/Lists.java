package com.sparta.springusersetting.domain.lists.entity;

import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "lists")
public class Lists extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String createdBy;

    private String modifiedBy;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;
}
