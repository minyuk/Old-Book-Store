package com.personal.oldbookstore.domain.item.dto;

import lombok.Builder;

@Builder
public record ItemIndexResponseDto(
        Long id,
        String name,
        Integer price,
        String fileName
) {
}
