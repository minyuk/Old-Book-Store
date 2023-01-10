package com.personal.oldbookstore.domain.order.service;

import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.service.ItemService;
import com.personal.oldbookstore.domain.order.dto.OrderItemRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.entity.Order;
import com.personal.oldbookstore.domain.order.entity.OrderItem;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemService itemService;

    public void cancel(Long orderId) {
        Order order = findOrder(orderId);

        order.cancel();
    }

    public Long create(User user, OrderRequestDto dto) {

        List<OrderItem> orderItems = createOrderItems(dto);

        Order order = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .recipient(dto.recipient())
                .phone(dto.phone())
                .address(dto.address())
                .payment(dto.payment())
                .build();

        return orderRepository.save(order).getId();
    }

    private Order findOrder(Long id) {
        return orderRepository.findByIdWithFetchJoinOrderItem(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );
    }

    private List<OrderItem> createOrderItems(OrderRequestDto dto) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto orderItem : dto.orderItems()) {
            Item item = itemService.findItem(orderItem.itemId());

            orderItems.add(OrderItem.builder()
                    .item(item)
                    .orderPrice(item.getPrice())
                    .count(orderItem.count())
                    .build());

            item.decreaseStock(orderItem.count());
            item.updateSaleStatus();
        }

        return orderItems;
    }

}
