package com.personal.oldbookstore.domain.item.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ItemListResponseDto(
        Long id,
        String seller,
        String name,
        String category,
        Long viewCount,
        String saleStatus,
        String createdDate
) {
}
