package com.personal.oldbookstore.domain.item.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Category {
    NOBEL("소설"),
    DEVELOPMENT("자기계발"),
    IT("IT"),
    CARTOON("만화"),
    TEST("테스트");

    private final String value;
}
