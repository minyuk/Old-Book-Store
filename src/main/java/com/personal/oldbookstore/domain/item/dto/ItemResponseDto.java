package com.personal.oldbookstore.domain.item.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemResponseDto(
        Long id,
        String seller,
        String name,
        String category,
        String bookTitle,
        String bookAuthor,
        String contents,
        Integer stock,
        Integer price,
        Long viewCount,
        String saleStatus,
        LocalDateTime createdDate
) {
}
