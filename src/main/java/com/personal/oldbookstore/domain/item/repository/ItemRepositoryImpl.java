package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.entity.QItem;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.personal.oldbookstore.domain.item.entity.QItem.item;
import static com.personal.oldbookstore.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class ItemRepositoryImpl implements ItemRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Item> findAllBySearchOption(Pageable pageable, String category, String keyword) {
        List<Item> items = queryFactory.selectFrom(item)
                .where(eqCategory(category), containsKeyword(keyword))
                .join(item.user, user)
                .fetchJoin()
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(item.id.desc())
                .fetch();

        Long total = queryFactory.select(item.count())
                .from(item)
                .where(eqCategory(category))
                .fetchOne();

        return new PageImpl<>(items, pageable, total);
    }

    @Override
    public Optional<Item> findByIdWithFetchJoinUser(Long id) {
        Item item = queryFactory.selectFrom(QItem.item)
                .join(QItem.item.user, user)
                .fetchJoin()
                .where(QItem.item.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(item);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) return null;
        return item.name.contains(keyword)
                .or(item.bookTitle.contains(keyword))
                .or(item.bookAuthor.contains(keyword));
    }

    private BooleanExpression eqCategory(String category) {
        if (category == null || category.isEmpty()) return null;
        return item.category.eq(Category.valueOf(category));
    }
}