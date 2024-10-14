package com.sparta.springusersetting.domain.card.repository;

import com.sparta.springusersetting.domain.card.entity.ActivityLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ActivityLogRepository extends JpaRepository<ActivityLog,Long>
{

}