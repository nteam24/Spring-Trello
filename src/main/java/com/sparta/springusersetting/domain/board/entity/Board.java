package com.sparta.springusersetting.domain.board.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Getter
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

//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Card> cards; // card 엔티티 푸쉬되면 다시 "card" 단어 적어주세요

//    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<List> lists; // 보드에 속한 리스트들 (리스트와 카드의 관계 정의

    public Board(String title, String backgroundColor, String backgroundImageUrl, Workspace workspace) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.workspace = workspace;
    }

    public Board() {}
}
