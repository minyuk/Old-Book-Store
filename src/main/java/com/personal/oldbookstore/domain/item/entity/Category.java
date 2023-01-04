package com.personal.oldbookstore.domain.item.entity;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Category {
    NOBEL("소설"),
    DEVELOPMENT("자기계발"),
    IT("IT"),
    CARTOON("만화");

    private final String value;
}
