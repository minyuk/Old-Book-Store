package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.personal.oldbookstore.domain.basket.entity.QBasket.basket;
import static com.personal.oldbookstore.domain.item.entity.QItem.item;

@RequiredArgsConstructor
public class BasketRepositoryImpl implements BasketRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<Basket> findByUserIdAndItemId(Long userId, Long itemId) {
        Basket findBasket = queryFactory.selectFrom(basket)
                .join(basket.item, item)
                .fetchJoin()
                .where(basket.user.id.eq(userId).and(basket.item.id.eq(itemId)))
                .fetchOne();

        return Optional.ofNullable(findBasket);
    }

}
