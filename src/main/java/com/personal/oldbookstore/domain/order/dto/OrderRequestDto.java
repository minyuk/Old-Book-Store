package com.personal.oldbookstore.domain.order.dto;

import com.personal.oldbookstore.domain.order.entity.Address;
import com.personal.oldbookstore.domain.order.entity.Payment;

import java.util.List;

public record OrderRequestDto(
        List<OrderItemRequestDto> orderItems,
        String recipient,
        String phone,
        Address address,
        Payment payment
) {
}
