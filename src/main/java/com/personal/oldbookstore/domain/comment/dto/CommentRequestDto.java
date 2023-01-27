package com.personal.oldbookstore.domain.comment.dto;

public record CommentRequestDto(
        Integer depth,
        String contents,
        Long parentId
) {
}
