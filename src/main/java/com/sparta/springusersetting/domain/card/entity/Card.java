package com.sparta.springusersetting.domain.card.entity;


import com.sparta.springusersetting.attachment.entity.Attachment;
import com.sparta.springusersetting.domain.comment.entity.Comment;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User manager;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lists_id", nullable = false)
    private Lists lists;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ActivityLog> activityLogs = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> files = new ArrayList<>();



    public Card(String title, String contents, LocalDate deadline, User manager, Lists lists)
    {
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
        this.manager = manager;
        this.lists = lists;
    }
    public void update(User manager, Lists lists, String title, String contents, LocalDate deadline)
    {
        this.manager = manager;
        this.lists = lists;
        this.title = title;
        this.contents = contents;
        this.deadline = deadline;
    }







}
