package com.sparta.springusersetting.domain.list.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "lists")
public class List extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String createdBy;

    private String modifiedBy;
}
