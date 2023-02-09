package com.personal.oldbookstore.domain.comment.repository;

import com.personal.oldbookstore.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> findAllByItemId(Long itemId);

    Page<Comment> findAllByUserId(Long userId, Pageable pageable);

}
