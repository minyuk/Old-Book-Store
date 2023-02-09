package com.personal.oldbookstore.domain.order.dto;

import lombok.Builder;

@Builder
public record OrderItemResponseDto(
        Long id,
        Long itemId,
        String name,
        String bookTitle,
        Integer itemPrice,
        Integer count,
        Integer orderPrice
) {
}
