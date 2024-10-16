package com.sparta.springusersetting.domain.card.repository;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.springusersetting.domain.card.dto.CardSearchRequestDto;
import com.sparta.springusersetting.domain.card.dto.CardSearchResponseDto;
import com.sparta.springusersetting.domain.card.dto.QCardSearchResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

import static com.sparta.springusersetting.domain.card.entity.QCard.card;
import static com.sparta.springusersetting.domain.lists.entity.QLists.lists;
import static com.sparta.springusersetting.domain.user.entity.QUser.user;

public class CardRepositoryCustomImpl implements CardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public CardRepositoryCustomImpl(JPAQueryFactory jpaQueryFactory) {
        this.queryFactory = jpaQueryFactory;
    }

    @Override
    public Page<CardSearchResponseDto> searchCards(CardSearchRequestDto searchRequest, Pageable pageable) {
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
                .leftJoin(card.lists, lists)
                .leftJoin(card.manager, user)
                .where(
                        workspaceIdEq(searchRequest.getWorkspaceId()),
                        boardIdEq(searchRequest.getBoardId()),
                        titleContains(searchRequest.getTitle()),
                        contentsContains(searchRequest.getContents()),
                        deadlineEq(searchRequest.getDeadline()),
                        managerEmailContains(searchRequest.getManagerEmail())
                )
                .orderBy(getOrderSpecifier(pageable.getSort()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory
                .select(card.countDistinct())
                .from(card)
                .leftJoin(card.lists, lists)
                .leftJoin(card.manager, user)
                .where(
                        workspaceIdEq(searchRequest.getWorkspaceId()),
                        boardIdEq(searchRequest.getBoardId()),
                        titleContains(searchRequest.getTitle()),
                        contentsContains(searchRequest.getContents()),
                        deadlineEq(searchRequest.getDeadline()),
                        managerEmailContains(searchRequest.getManagerEmail())
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression workspaceIdEq(Long workspaceId) {
        return workspaceId != null ? card.lists.board.workspace.id.eq(workspaceId) : null;
    }

    private BooleanExpression boardIdEq(Long boardId) {
        return boardId != null ? card.lists.board.id.eq(boardId) : null;
    }

    private BooleanExpression titleContains(String title) {
        return StringUtils.hasText(title) ? card.title.containsIgnoreCase(title) : null;
    }
    private BooleanExpression contentsContains(String contents) {
        return StringUtils.hasText(contents) ? card.contents.containsIgnoreCase(contents) : null;
    }

    private BooleanExpression deadlineEq(LocalDate deadline) {
        return deadline != null ? card.deadline.eq(deadline) : null;
    }

    private BooleanExpression managerEmailContains(String managerEmail) {
        return StringUtils.hasText(managerEmail) ? card.manager.email.containsIgnoreCase(managerEmail) : null;
    }

    private OrderSpecifier<?> getOrderSpecifier(Sort sort) {
        if (sort.isEmpty()) {
            return card.createdAt.desc();
        }

        for (Sort.Order order : sort) {
            switch (order.getProperty()) {
                case "title":
                    return order.isAscending() ? card.title.asc() : card.title.desc();
                case "deadline":
                    return order.isAscending() ? card.deadline.asc() : card.deadline.desc();
                case "createdAt":
                    return order.isAscending() ? card.createdAt.asc() : card.createdAt.desc();
                default:
                    return card.createdAt.desc();
            }
        }

        return card.createdAt.desc();
    }
}
