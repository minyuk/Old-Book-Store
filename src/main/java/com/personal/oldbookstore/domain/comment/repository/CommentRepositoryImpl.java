package com.personal.oldbookstore.domain.comment.repository;

import com.personal.oldbookstore.domain.comment.entity.Comment;
import com.personal.oldbookstore.domain.item.entity.QItem;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

import static com.personal.oldbookstore.domain.comment.entity.QComment.comment;
import static com.personal.oldbookstore.domain.item.entity.QItem.item;
import static com.personal.oldbookstore.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Comment> findAllByItemId(Long itemId) {
        return queryFactory.selectFrom(comment)
                .join(comment.user, user)
                .fetchJoin()
                .where(comment.item.id.eq(itemId))
                .orderBy(comment.id.desc())
                .fetch();
    }

    @Override
    public Page<Comment> findAllByUserId(Long userId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment)
                .join(comment.item, item)
                .fetchJoin()
                .where(comment.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(sort(pageable))
                .fetch();

        Long total = queryFactory.select(comment.count())
                .from(comment)
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
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
                case "contents" : return new OrderSpecifier(direction, comment.contents);
                case "createdDate" : return new OrderSpecifier(direction, item.createdDate);
            }
        }

        return null;
    }
}
