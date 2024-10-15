package com.sparta.springusersetting.domain.lists.repository;

import com.sparta.springusersetting.domain.board.entity.Board;
import com.sparta.springusersetting.domain.lists.entity.Lists;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ListsRepository extends JpaRepository<Lists, Long> {

    @Query("SELECT MAX(l.pos) FROM Lists l WHERE l.board = :board")
    Optional<Integer> findMaxPositionByBoard(@Param("board") Board board);

    @Query("SELECT l FROM Lists l WHERE l.id = :listsId AND l.board.id = :boardId")
    Optional<Lists> findByIdAndBoardId(@Param("listsId") Long listsId, @Param("boardId") Long boardId);

    @Query("SELECT l FROM Lists l WHERE l.board = :board ORDER BY l.pos")
    List<Lists> findAllByBoardOrderByPos(@Param("board") Board board);

    @Query("SELECT l FROM Lists l WHERE l.board = :board AND l.pos > :pos ORDER BY l.pos")
    List<Lists> findAllByBoardAndPosGreaterThanOrderByPos(@Param("board") Board board, @Param("pos") int pos);
}
