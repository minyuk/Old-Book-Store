package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
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

    @Override
    public Page<Basket> findAllByUserId(Long id, Pageable pageable) {
        List<Basket> baskets = queryFactory.selectFrom(basket)
                .join(basket.item, item)
                .fetchJoin()
                .where(basket.user.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(basket.id.desc())
                .fetch();

        Long total = queryFactory.select(basket.count())
                .from(basket)
                .fetchOne();

        return new PageImpl<>(baskets, pageable, total);
    }

}
