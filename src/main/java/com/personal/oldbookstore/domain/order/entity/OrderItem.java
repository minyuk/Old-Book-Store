package com.personal.oldbookstore.domain.order.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.order.dto.OrderItemResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class OrderItem extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Integer count;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public OrderItem(Item item, Integer count, Integer orderPrice) {
        this.item = item;
        this.count = count;
        this.orderPrice = orderPrice * count;
    }

    public OrderItemResponseDto toDto() {
        return OrderItemResponseDto.builder()
                .id(id)
                .itemId(item.getId())
                .name(item.getName())
                .bookTitle(item.getBookTitle())
                .itemPrice(item.getPrice())
                .count(count)
                .orderPrice(orderPrice)
                .build();
    }

    public void cancel() {
        getItem().incrementStock(count);
        getItem().updateSaleStatus();
    }

}
