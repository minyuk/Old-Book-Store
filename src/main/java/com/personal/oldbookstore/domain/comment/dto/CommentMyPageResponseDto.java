package com.personal.oldbookstore.domain.comment.dto;

import lombok.Builder;

@Builder
public record CommentMyPageResponseDto(
        Long itemId,
        String itemName,
        String contents,
        String createdDate,
        String saleStatus
) {
}
