package com.sparta.springusersetting.domain.lists.repository;

import com.sparta.springusersetting.domain.lists.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListsRepository extends JpaRepository<Lists, Long> {

}
