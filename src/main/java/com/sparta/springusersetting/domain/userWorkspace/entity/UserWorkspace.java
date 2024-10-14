package com.sparta.springusersetting.domain.userWorkspace.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class UserWorkspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;
}
