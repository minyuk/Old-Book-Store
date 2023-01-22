package com.personal.oldbookstore.domain.comment.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CommentResponseDto(
        Long id,
        String writer,
        String contents,
        Integer depth,
        Long parentId,
        Boolean viewStatus,
        String createdDate
) {
}
