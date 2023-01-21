package com.personal.oldbookstore.domain.comment.repository;

import com.personal.oldbookstore.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {

    Page<Comment> findAllByItemId(Long itemId, Pageable pageable);

}
