package com.personal.oldbookstore.domain.like.repository;

import com.personal.oldbookstore.domain.like.entity.LikeItem;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

import static com.personal.oldbookstore.domain.item.entity.QItem.item;
import static com.personal.oldbookstore.domain.like.entity.QLikeItem.likeItem;

@RequiredArgsConstructor
public class LikeItemRepositoryImpl implements LikeItemRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<LikeItem> findByIdWithFetchJoinItem(Long id) {
        LikeItem findLikeItem = queryFactory.selectFrom(likeItem)
                .join(likeItem.item, item)
                .fetchJoin()
                .where(likeItem.id.eq(id))
                .fetchOne();

        return Optional.ofNullable(findLikeItem);
    }

    @Override
    public Page<LikeItem> findAllByUserId(Long id, Pageable pageable) {
        List<LikeItem> likeItems = queryFactory.selectFrom(likeItem)
                .join(likeItem.item, item)
                .fetchJoin()
                .where(likeItem.user.id.eq(id))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sort(pageable))
                .fetch();

        Long total = queryFactory.select(likeItem.count())
                .from(likeItem)
                .fetchOne();

        return new PageImpl<>(likeItems, pageable, total);
    }

    @Override
    public Optional<LikeItem> findByUserIdAndItemId(Long userId, Long itemId) {
        LikeItem findLikeItem = queryFactory.selectFrom(likeItem)
                .join(likeItem.item, item)
                .fetchJoin()
                .where(likeItem.user.id.eq(userId).and(likeItem.item.id.eq(itemId)))
                .fetchOne();

        return Optional.ofNullable(findLikeItem);
    }

    private OrderSpecifier<?> sort(Pageable pageable){
        if(pageable.getSort().isEmpty()){
            return new OrderSpecifier<>(Order.DESC, item.id);
        }

        Sort sort = pageable.getSort();
        String field = "";
        Order direction = null;

        //sort가 여러개일 경우
        for (Sort.Order order : sort) {
            field = order.getProperty();
            direction = order.getDirection().isAscending() ? Order.ASC : Order.DESC;

            switch (field){
                case "name" : return new OrderSpecifier(direction, item.name);
                case "saleStatus" : return new OrderSpecifier(direction, item.saleStatus);
                case "bookTitle" : return new OrderSpecifier(direction, item.bookTitle);
                case "price" : return new OrderSpecifier(direction, item.price);
            }
        }

        return null;
    }
}
