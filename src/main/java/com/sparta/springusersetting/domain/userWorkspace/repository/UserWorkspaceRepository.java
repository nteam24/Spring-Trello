package com.sparta.springusersetting.domain.userWorkspace.repository;

import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {

    @Query("SELECT u FROM UserWorkspace u WHERE u.user.id = :userId AND u.workspace.id = :workspaceId")
    Optional<UserWorkspace> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);

}
