package com.personal.oldbookstore.domain.order.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Payment {
    CARD("체크/신용카드"),
    DEPOSIT("무통장입금"),
    MOBILE("휴대폰결제");

    private final String value;
}
