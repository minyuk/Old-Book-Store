package com.personal.oldbookstore.domain.like.dto;

import lombok.Builder;

@Builder
public record LikeItemResponseDto(
        Long id,
        String name,
        String bookTitle,
        Integer price
) {
}
