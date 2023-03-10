package com.personal.oldbookstore.domain.comment.repository;

import com.personal.oldbookstore.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
}
