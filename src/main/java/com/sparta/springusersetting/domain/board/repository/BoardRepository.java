package com.sparta.springusersetting.domain.board.repository;

import com.sparta.springusersetting.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board,Long> {
}
