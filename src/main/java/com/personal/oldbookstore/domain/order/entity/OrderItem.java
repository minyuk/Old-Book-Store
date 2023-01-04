package com.personal.oldbookstore.domain.order.entity;

import com.personal.oldbookstore.domain.item.entity.Item;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long id;

    @Column(nullable = false)
    private Integer orderPrice;

    @Column(nullable = false)
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @Builder
    public OrderItem(Long id, Integer orderPrice, Integer count) {
        this.id = id;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
