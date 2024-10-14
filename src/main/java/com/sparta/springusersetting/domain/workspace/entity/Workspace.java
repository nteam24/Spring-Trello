package com.sparta.springusersetting.domain.workspace.entity;

import com.sparta.springusersetting.domain.common.entity.Timestamped;
import com.sparta.springusersetting.domain.participation.entity.Participation;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
public class Workspace extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    
    private String description;

    @OneToMany(mappedBy = "workspace",cascade = CascadeType.REMOVE)
    private List<Participation> participationList;

    public Workspace(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public void updateWorkspace(String name, String description){
        this.name = name;
        this.description = description;
    }
}
