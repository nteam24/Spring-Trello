package com.sparta.springusersetting.domain.userWorkspace.repository;

import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {
}
