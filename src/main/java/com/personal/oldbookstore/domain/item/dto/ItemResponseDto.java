package com.personal.oldbookstore.domain.item.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@NoArgsConstructor
public class ItemResponseDto{
    private Long id;
    private String seller;
    private String name;
    private String category;
    private String bookTitle;
    private String bookAuthor;
    private String contents;
    private Integer stock;
    private Integer price;
    private Long viewCount;
    private String saleStatus;
    private String createdDate;
    private Boolean likeStatus = false;

    @Builder
    public ItemResponseDto(Long id, String seller, String name, String category, String bookTitle, String bookAuthor,
                           String contents, Integer stock, Integer price, Long viewCount, String saleStatus, LocalDateTime createdDate) {
        this.id = id;
        this.seller = seller;
        this.name = name;
        this.category = category;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.contents = contents;
        this.stock = stock;
        this.price = price;
        this.viewCount = viewCount;
        this.saleStatus = saleStatus;
        this.createdDate = createdDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"));
    }

    public void setLikeStatus(Boolean status) {
        this.likeStatus = status;
    }
}
