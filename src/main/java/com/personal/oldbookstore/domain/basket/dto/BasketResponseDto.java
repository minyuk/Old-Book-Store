package com.personal.oldbookstore.domain.basket.dto;

import com.personal.oldbookstore.domain.item.entity.SaleStatus;
import lombok.Builder;

@Builder
public record BasketResponseDto(
        Long id,
        Long itemId,
        String name,
        String bookTitle,
        Integer itemPrice,
        Integer itemStock,
        Integer count,
        SaleStatus saleStatus,
        Integer totalPrice
) {
}
