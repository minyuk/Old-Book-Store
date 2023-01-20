package com.personal.oldbookstore.domain.order.dto;

import com.personal.oldbookstore.domain.order.entity.OrderStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderListResponseDto(
        Long id,
        OrderStatus orderStatus,
        LocalDateTime orderDate,
        Integer totalPrice,
        List<OrderItemResponseDto> orderItemResponseDtos

) {
}
