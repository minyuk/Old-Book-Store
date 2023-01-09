package com.personal.oldbookstore.domain.order.dto;

public record OrderItemRequestDto(
        Long itemId,
        Integer count

) {
}
