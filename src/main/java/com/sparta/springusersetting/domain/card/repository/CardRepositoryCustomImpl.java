package com.sparta.springusersetting.domain.card.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import com.sparta.springusersetting.domain.card.dto.QCardSearchResponseDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.sparta.springusersetting.domain.board.entity.QBoard.board;
import static com.sparta.springusersetting.domain.card.entity.QCard.card;
import static com.sparta.springusersetting.domain.lists.entity.QLists.lists;
import static com.sparta.springusersetting.domain.user.entity.QUser.user;
import static com.sparta.springusersetting.domain.workspace.entity.QWorkspace.workspace;

public class CardRepositoryCustomImpl implements CardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public CardRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Slice<CardSearchResponseDto> searchCards(CardSearchRequestDto searchRequest, Long cursorId, int pageSize) {
        List<CardSearchResponseDto> content = queryFactory
                .select(new QCardSearchResponseDto(
                        card.id,
                        card.title,
                        card.contents,
                        card.deadline,
                        card.manager.email,
                        card.createdAt
                ))
                .distinct()
                .from(card)
                .innerJoin(card.lists, lists)
                .innerJoin(lists.board, board)
                .innerJoin(board.workspace, workspace)
                .innerJoin(card.manager, user)
                .where(
                        cursorIdGt(cursorId),
                        workspaceIdEq(searchRequest.getWorkspaceId()),
                        boardIdEq(searchRequest.getBoardId()),
                        keywordContains(searchRequest.getKeyword()),
                        deadlineEq(searchRequest.getDeadline()),
                        managerEmailContains(searchRequest.getManagerEmail())
                )
                .orderBy(card.id.asc())
                .limit(pageSize + 1)
                .fetch();

        boolean hasNext = content.size() > pageSize;
        if (hasNext){
            content.remove(pageSize);
        }

        return new SliceImpl<>(content, Pageable.unpaged(), hasNext);
    }

    private BooleanExpression cursorIdGt(Long cursorId) {
        return cursorId != null ? card.id.gt(cursorId) : null;
    }

    private BooleanExpression workspaceIdEq(Long workspaceId) {
        return workspaceId != null ? workspace.id.eq(workspaceId) : null;
    }

    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? board.id.eq(boardId) : null;
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? card.title.containsIgnoreCase(title) : null;
    }
    private BooleanExpression contentsContains(String contents) {
        return StringUtils.hasText(contents) ? card.contents.containsIgnoreCase(contents) : null;
    }
    // title, contents 통합검색 표현식
    private BooleanExpression keywordContains(String keyword) {
        return StringUtils.hasText(keyword) ? card.title.containsIgnoreCase(keyword).or(card.contents.containsIgnoreCase(keyword))
                : null;
    }

    private BooleanExpression deadlineEq(LocalDate deadline) {
        return deadline != null ? card.deadline.eq(deadline) : null;
    }

    private BooleanExpression managerEmailContains(String managerEmail) {
        return StringUtils.hasText(managerEmail) ? card.manager.email.containsIgnoreCase(managerEmail) : null;
    }
}
