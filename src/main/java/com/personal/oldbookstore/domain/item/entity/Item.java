package com.personal.oldbookstore.domain.item.entity;

import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;

@Getter
@NoArgsConstructor
@Entity
public class Item {

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

    @Enumerated(EnumType.STRING)
    private SaleStatus saleStatus;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Item(User user, String name, Category category, String bookTitle, String bookAuthor, String contents, Integer stock, Integer price, Long viewCount, SaleStatus saleStatus) {
        this.user = user;
        this.name = name;
        this.category = category;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.contents = contents;
        this.stock = stock;
        this.price = price;
        this.viewCount = viewCount == null ? 0 : viewCount;
        this.saleStatus = saleStatus == null ? SaleStatus.SALE : saleStatus;
    }

    public void updateItem(ItemRequestDto dto) {
        this.name = dto.name();
        this.category = dto.category();
        this.bookTitle = dto.bookTitle();
        this.bookAuthor = dto.bookAuthor();
        this.contents = dto.contents();
        this.stock = dto.stock();
        this.price = dto.price();
    }

    public void incrementViewCount() {
        viewCount += 1;
    }

    public void updateSaleStatus() {
        if (stock > 0) saleStatus = SaleStatus.SALE;
        else saleStatus = SaleStatus.SOLD_OUT;
    }


}
