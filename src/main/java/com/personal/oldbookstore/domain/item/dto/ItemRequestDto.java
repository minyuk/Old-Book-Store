package com.personal.oldbookstore.domain.item.dto;

import com.personal.oldbookstore.domain.item.entity.Category;
import lombok.Builder;

import javax.validation.constraints.*;

@Builder
public record ItemRequestDto(
        @Size(min = 3, max = 30, message = "제목은 3자 이상 30자 이하로 입력해주세요.")
        String name,

        @NotNull
        Category category,

        @NotBlank
        String bookTitle,

        @NotBlank
        String bookAuthor,

        @NotBlank
        String contents,

        @Max(value = 100, message = "재고는 100개 이하 입력해주세요.")
        @NotNull(message = "재고를 입력해주세요.")
        Integer stock,

        @Min(value = 1000, message = "가격은 1,000원 이상 입력해주세요.")
        @Max(value = 1000000, message = "가격은 1,000,000원 이하 입력해주세요.")
        @NotNull(message = "가격을 입력해주세요.")
        Integer price
)
{
}
