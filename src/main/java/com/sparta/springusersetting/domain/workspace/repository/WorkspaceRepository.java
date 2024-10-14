package com.sparta.springusersetting.domain.workspace.repository;

import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace,Long> {
}
