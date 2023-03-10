package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

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
                .orderBy(sort(pageable))
                .fetch();

        Long total = queryFactory.select(item.count())
                .from(item)
                .where(eqCategory(category), containsKeyword(keyword))
                .fetchOne();

        return new PageImpl<>(items, pageable, total);
    }

    @Override
    public Page<Item> findAllByUserId(Long userId, Pageable pageable) {
        List<Item> items = queryFactory.selectFrom(item)
                .where(item.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sort(pageable))
                .fetch();

        Long total = queryFactory.select(item.count())
                .from(item)
                .where(item.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(items, pageable, total);

    }

    @Override
    public List<Item> findAllByCategory(String category) {
        return queryFactory.selectFrom(item)
                .where(eqCategory(category))
                .join(item.user, user)
                .fetchJoin()
                .orderBy(item.createdDate.desc())
                .fetch();
    }

    @Override
    public Optional<Item> findByIdWithFetchJoinUser(Long id) {
        Item findItem = queryFactory.selectFrom(item)
                .join(item.user, user)
                .fetchJoin()
                .where(item.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findItem);
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

    private OrderSpecifier<?> sort(Pageable pageable){
        if(pageable.getSort().isEmpty()){
            return new OrderSpecifier<>(Order.DESC, item.id);
        }

        Sort sort = pageable.getSort();
        String field = "";
        Order direction = null;

        //sort??? ???????????? ??????
        for (Sort.Order order : sort) {
            field = order.getProperty();
            direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            switch (field){
                case "name" : return new OrderSpecifier(direction, item.name);
                case "saleStatus" : return new OrderSpecifier(direction, item.saleStatus);
                case "seller" : return new OrderSpecifier(direction, item.user);
                case "createdDate" : return new OrderSpecifier(direction, item.createdDate);
                case "viewCount" : return new OrderSpecifier(direction, item.viewCount);
            }
        }

        return null;
    }
}
