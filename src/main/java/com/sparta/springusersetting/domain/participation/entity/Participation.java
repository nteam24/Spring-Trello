package com.sparta.springusersetting.domain.participation.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.user.entity.User;
import com.sparta.springusersetting.domain.user.enums.MemberRole;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static com.sparta.springusersetting.domain.user.enums.MemberRole.*;

@Getter
@Entity
@NoArgsConstructor
public class Participation extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @Enumerated(EnumType.STRING)
    private MemberRole memberRole;

    private boolean activation;


    public Participation(User user, Workspace workspace) {
        this.user = user;
        this.workspace = workspace;
        this.memberRole = ROLE_READ_USER;
        this.activation = false;
    }


    public void UserBeActive(){
        this.activation=true;
    }

    public void changeRole(MemberRole memberRole) {
        this.memberRole = memberRole;
    }
}
