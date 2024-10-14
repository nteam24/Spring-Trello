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

    @ManyToOne
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;
}
