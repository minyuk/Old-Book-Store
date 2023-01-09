package com.personal.oldbookstore.domain.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SaleStatus {
    SALE("판매중"),
    SOLD_OUT("품절");

    private final String value;
}
