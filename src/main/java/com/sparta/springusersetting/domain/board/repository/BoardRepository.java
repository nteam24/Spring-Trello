package com.sparta.springusersetting.domain.board.repository;

import com.sparta.springusersetting.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long> {

    List<Board> findByWorkspaceId(Long workspaceId);
}
