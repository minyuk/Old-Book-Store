package com.personal.oldbookstore.domain.comment.repository;

import com.personal.oldbookstore.domain.comment.entity.Comment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static com.personal.oldbookstore.domain.comment.entity.QComment.comment;
import static com.personal.oldbookstore.domain.user.entity.QUser.user;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Comment> findAllByItemId(Long itemId, Pageable pageable) {
        List<Comment> comments = queryFactory.selectFrom(comment)
                .join(comment.user, user)
                .fetchJoin()
                .where(comment.item.id.eq(itemId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(comment.id.desc())
                .fetch();

        Long total = queryFactory.select(comment.count())
                .from(comment)
                .fetchOne();

        return new PageImpl<>(comments, pageable, total);
    }
}
