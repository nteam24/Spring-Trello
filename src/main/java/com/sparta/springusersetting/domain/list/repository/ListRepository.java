package com.sparta.springusersetting.domain.list.repository;

import com.sparta.springusersetting.domain.list.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {

}
