package com.sparta.springusersetting.domain.workspace.repository;

import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {


    Optional<Workspace> findByName(String name);
}
