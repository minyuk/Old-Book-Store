package com.personal.oldbookstore.domain.item.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.comment.entity.Comment;
import com.personal.oldbookstore.domain.item.dto.*;
import com.personal.oldbookstore.domain.like.entity.LikeItem;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class Item extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    @Column(nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private String bookAuthor;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contents;

    @Column(nullable = false)
    private Integer stock;

    @Column(nullable = false)
    private Integer price;

    private Long viewCount;

    private Long likeCount;

    private Long commentCount;

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "item", fetch = LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ItemFile> files = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LikeItem> likeItems = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Basket> baskets = new ArrayList<>();

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Builder
    public Item(User user, String name, Category category, String bookTitle, String bookAuthor, String contents,
                Integer stock, Integer price, Long viewCount, Long likeCount, Long commentCount, SaleStatus saleStatus) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.contents = contents;
        this.stock = stock;
        this.price = price;
        this.viewCount = viewCount == null ? 0 : viewCount;
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.commentCount = commentCount == null ? 0 : commentCount;
        this.saleStatus = saleStatus == null ? SaleStatus.SALE : saleStatus;
    }

    public void incrementViewCount() {
        viewCount++;
    }

    public void decreaseStock(int orderQuantity) {
        int rest = stock - orderQuantity;
        if (rest < 0) {
            throw new CustomException(ErrorCode.STOCK_FAIL);
        }

        stock = rest;
    }

    public void incrementStock(int cancelQuantity) {
        stock += cancelQuantity;
    }

    public void incrementLikeCount() { likeCount++; }

    public void decreaseLikeCount() { likeCount--; }

    public void incrementCommentCount() {
        commentCount++;
    }

    public void updateSaleStatus() {
        if (stock > 0) saleStatus = SaleStatus.SALE;
        else saleStatus = SaleStatus.SOLD_OUT;
    }

    public void updateItem(ItemUpdateRequestDto dto) {
        this.name = dto.name();
        this.category = dto.category();
        this.bookTitle = dto.bookTitle();
        this.bookAuthor = dto.bookAuthor();
        this.contents = dto.contents();
        this.stock = dto.stock();
        this.price = dto.price();
        this.saleStatus = dto.saleStatus();
    }

    public ItemResponseDto toDto() {
        return ItemResponseDto.builder()
                .id(id)
                .seller(user.getNickname())
                .name(name)
                .category(getCategory().getValue())
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .viewCount(viewCount)
                .likeCount(likeCount)
                .saleStatus(getSaleStatus().getValue())
                .createdDate(getModifiedDate())
                .files(files.stream().map(ItemFile::toDto).toList())
                .build();
    }

    public ItemListResponseDto toDtoList() {
        return ItemListResponseDto.builder()
                .id(id)
                .seller(user.getNickname())
                .name(name)
                .category(getCategory().getValue())
                .viewCount(viewCount)
                .commentCount(commentCount)
                .saleStatus(getSaleStatus().getValue())
                .createdDate(getModifiedDate().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                .build();
    }

    public ItemIndexResponseDto toDtoIndex() {
        return ItemIndexResponseDto.builder()
                .id(id)
                .name(name)
                .price(price)
                .fileName(files.get(0).getFileName())
                .build();
    }



}
