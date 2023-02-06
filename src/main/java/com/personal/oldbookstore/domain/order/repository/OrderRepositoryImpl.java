package com.personal.oldbookstore.domain.order.repository;

import com.personal.oldbookstore.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static com.personal.oldbookstore.domain.item.entity.QItem.item;
import static com.personal.oldbookstore.domain.order.entity.QOrder.order;
import static com.personal.oldbookstore.domain.order.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Order> findAllByUserId(Long userId, Pageable pageable) {
        List<Order> orders = queryFactory.selectFrom(order)
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(orderItem.item, item)
                .fetchJoin()
                .where(order.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(order.id.desc())
                .fetch();

        Long total = queryFactory.select(order.count())
                .from(order)
                .fetchOne();

        return new PageImpl<>(orders, pageable, total);
    }

    @Override
    public Optional<Order> findByIdWithFetchJoinOrderItem(Long id) {
        Order findOrder = queryFactory.selectFrom(order)
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(orderItem.item, item)
                .fetchJoin()
                .where(order.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findOrder);
    }

    @Override
    public List<Order> findAllByItemId(Long itemId) {
        return queryFactory.selectFrom(order)
                .join(order.orderItems, orderItem)
                .fetchJoin()
                .join(orderItem.item, item)
                .fetchJoin()
                .where(orderItem.item.id.eq(itemId))
                .fetch();
    }
}
