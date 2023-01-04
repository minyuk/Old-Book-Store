package com.personal.oldbookstore.domain.basket.entity;

import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Basket {

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
    public Basket(Long id, Integer count) {
        this.id = id;
        this.count = count;
    }
}
