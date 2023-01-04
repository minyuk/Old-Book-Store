package com.personal.oldbookstore.domain.order.entity;

import com.personal.oldbookstore.domain.item.entity.SaleStatus;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Table(name = "orders")
@Getter
@NoArgsConstructor
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(length = 11, nullable = false)
    private String phone;

    @Column(nullable = false)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public Order(Long id, String recipient, String phone, String address, Payment payment, OrderStatus orderStatus) {
        this.id = id;
        this.recipient = recipient;
        this.phone = phone;
        this.address = address;
        this.payment = payment;
        this.orderStatus = orderStatus == null ? OrderStatus.ORDER : orderStatus;
    }
}
