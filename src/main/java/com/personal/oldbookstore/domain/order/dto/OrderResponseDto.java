package com.personal.oldbookstore.domain.order.dto;

import com.personal.oldbookstore.domain.order.entity.Address;
import com.personal.oldbookstore.domain.order.entity.OrderStatus;
import com.personal.oldbookstore.domain.order.entity.Payment;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record OrderResponseDto(
        Long id,
        String recipient,
        String phone,
        Address address,
        Payment payment,
        OrderStatus orderStatus,
        LocalDateTime orderDate,
        List<OrderItemResponseDto> orderItemResponseDtos,
        Integer totalPrice
) {
}
