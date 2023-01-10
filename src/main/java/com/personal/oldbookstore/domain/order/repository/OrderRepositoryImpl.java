package com.personal.oldbookstore.domain.order.repository;

import com.personal.oldbookstore.domain.order.entity.Order;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.personal.oldbookstore.domain.item.entity.QItem.item;
import static com.personal.oldbookstore.domain.order.entity.QOrder.order;
import static com.personal.oldbookstore.domain.order.entity.QOrderItem.orderItem;

@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

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
}
