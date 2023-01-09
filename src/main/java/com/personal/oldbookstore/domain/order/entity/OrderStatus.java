package com.personal.oldbookstore.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OrderStatus {
    ORDER("주문 완료"),
    CANCEL("주문 취소");

    private final String value;
}
