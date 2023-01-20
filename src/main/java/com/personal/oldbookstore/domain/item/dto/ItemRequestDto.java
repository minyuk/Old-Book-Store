package com.personal.oldbookstore.domain.item.dto;

import com.personal.oldbookstore.domain.item.entity.Category;
import lombok.Builder;

@Builder
public record ItemRequestDto(
    String name,
    Category category,
    String bookTitle,
    String bookAuthor,
    String contents,
    Integer stock,
    Integer price )
{
}
