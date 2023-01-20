package com.personal.oldbookstore.domain.basket.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Basket extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "basket_id")
    private Long id;

    @Column(nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public Basket(User user, Item item, Integer count) {
        this.user = user;
        this.item = item;
        this.count = count;
    }

    public BasketResponseDto toDto() {
        return BasketResponseDto.builder()
                .id(id)
                .itemId(item.getId())
                .name(item.getName())
                .bookTitle(item.getBookTitle())
                .itemPrice(item.getPrice())
                .itemStock(item.getStock())
                .saleStatus(item.getSaleStatus())
                .count(count)
                .totalPrice(getTotalPrice())
                .build();
    }

    public void updateCount(Integer count) {
        this.count = count;
    }

    private Integer getTotalPrice() {
        return item.getPrice() * count;
    }

}
