package com.sparta.springusersetting.domain.userWorkspace.repository;

import com.sparta.springusersetting.domain.userWorkspace.entity.UserWorkspace;
import com.sparta.springusersetting.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserWorkspaceRepository extends JpaRepository<UserWorkspace, Long> {

    @Query("SELECT DISTINCT u FROM UserWorkspace u WHERE u.user.id = :userId AND u.workspace.id = :workspaceId")
    Optional<UserWorkspace> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);

    @Query("SELECT DISTINCT u FROM UserWorkspace u left join fetch u.workspace w WHERE u.user.id=:userId")
    Page<UserWorkspace> findAllByUserId(Pageable pageable,
                                                  @Param("userId") Long userId);

}
