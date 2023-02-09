package com.personal.oldbookstore.domain.item.dto;

import lombok.Builder;

@Builder
public record ItemFileResponseDto(
        String imageUrl
) {
}
