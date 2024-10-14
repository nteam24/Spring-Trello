package com.sparta.springusersetting.domain.card.entity;


import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.thymeleaf.expression.Lists;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "cards")
@NoArgsConstructor
@Getter
public class Card extends Timestamped {


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @Column(nullable = false)
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User manager;

    @ManyToOne(mappedBy = "lists")
    @JoinColumn(name = "lists_id", nullable = false)
    private Lists lists;


    public Card(String title, String contents, LocalDate deadline, User manager)
    {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.manager = manager;
    }







}
