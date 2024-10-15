package com.sparta.springusersetting.domain.board.entity;

import com.sparta.springusersetting.domain.card.entity.Card;
import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor
@Table(name = "board")
public class Board extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = true)
    private String backgroundColor;

    @Column(nullable = true)
    private String backgroundImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Card> cards;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lists> lists;

    public Board(String title, String backgroundColor, String backgroundImageUrl, Workspace workspace) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.workspace = workspace;
    }

    // 보드의 기본 정보를 업데이트하는 메서드
    public void updateBoard(String title, String backgroundColor, String backgroundImageUrl) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
    }
}
