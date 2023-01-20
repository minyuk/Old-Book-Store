package com.personal.oldbookstore.domain.like.repository;

import com.personal.oldbookstore.domain.like.entity.LikeItem;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

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
                .orderBy(likeItem.id.desc())
                .fetch();

        Long total = queryFactory.select(likeItem.count())
                .from(likeItem)
                .fetchOne();

        return new PageImpl<>(likeItems, pageable, total);
    }
}
