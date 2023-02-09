package com.personal.oldbookstore.domain.order.entity;

import com.personal.oldbookstore.domain.base.BaseTimeEntity;
import com.personal.oldbookstore.domain.order.dto.OrderListResponseDto;
import com.personal.oldbookstore.domain.order.dto.OrderResponseDto;
import com.personal.oldbookstore.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Table(name = "Orders")
@Getter
@NoArgsConstructor
@Entity
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(nullable = false)
    private String recipient;

    @Column(length = 11, nullable = false)
    private String phone;

    @Embedded
    @Column(nullable = false)
    private Address address;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Payment payment;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(User user, List<OrderItem> orderItems, String recipient, String phone, Address address,
                 Payment payment, OrderStatus orderStatus, LocalDateTime orderDate) {
        this.user = user;
        addOrderItem(orderItems);
        this.recipient = recipient;
        this.phone = phone;
        this.address = address;
        this.payment = payment;
        this.orderStatus = orderStatus == null ? OrderStatus.ORDER : orderStatus;
        this.orderDate = orderDate == null ? LocalDateTime.now() : orderDate;
    }

    public OrderResponseDto toDto() {
        return OrderResponseDto.builder()
                .id(id)
                .recipient(recipient)
                .phone(phone)
                .address(address)
                .payment(payment.getValue())
                .orderStatus(orderStatus)
                .orderDate(orderDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                .totalPrice(getTotalPrice())
                .orderItemResponseDtos(orderItems.stream()
                        .map(OrderItem::toDto).collect(Collectors.toList()))
                .build();
    }

    public OrderListResponseDto toDtoList() {
        return OrderListResponseDto.builder()
                .id(id)
                .orderStatus(orderStatus)
                .orderDate(orderDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm")))
                .totalPrice(getTotalPrice())
                .orderItemResponseDtos(orderItems.stream()
                        .map(OrderItem::toDto).collect(Collectors.toList()))
                .build();
    }

    public void cancel() {
        this.orderStatus = OrderStatus.CANCEL;

        for(OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getOrderPrice();
        }
        return totalPrice;
    }

    private void addOrderItem (List<OrderItem> orderItems) {
        this.orderItems = orderItems;
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(this);
        }
    }



}
