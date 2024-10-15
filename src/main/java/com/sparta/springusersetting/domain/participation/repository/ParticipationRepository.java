package com.sparta.springusersetting.domain.participation.repository;

import com.sparta.springusersetting.domain.participation.entity.Participation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("SELECT DISTINCT u FROM Participation u WHERE u.user.id = :userId AND u.workspace.id = :workspaceId")
    Optional<Participation> findByUserIdAndWorkspaceId(@Param("userId") Long userId, @Param("workspaceId") Long workspaceId);

    @Query("SELECT DISTINCT u FROM Participation u left join fetch u.workspace w WHERE u.user.id=:userId")
    Page<Participation> findAllByUserId(Pageable pageable,
                                        @Param("userId") Long userId);

}
