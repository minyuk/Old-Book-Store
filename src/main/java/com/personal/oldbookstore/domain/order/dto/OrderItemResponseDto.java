package com.personal.oldbookstore.domain.order.dto;

import lombok.Builder;

@Builder
public record OrderItemResponseDto(
        Long id,
        String name,
        String bookTitle,
        Integer itemPrice,
        Integer count,
        Integer orderPrice
) {
}
