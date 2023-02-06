package com.personal.oldbookstore.domain.order.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.basket.repository.BasketRepository;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.order.dto.OrderItemRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderListResponseDto;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderResponseDto;
import com.personal.oldbookstore.domain.order.entity.Order;
import com.personal.oldbookstore.domain.order.entity.OrderItem;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    private final BasketRepository basketRepository;

    public Long create(PrincipalDetails principalDetails, OrderRequestDto dto) {

        List<OrderItem> orderItems = createOrderItems(dto);

        Order order = Order.builder()
                .user(principalDetails.getUser())
                .orderItems(orderItems)
                .recipient(dto.recipient())
                .phone(dto.phone())
                .address(dto.address())
                .payment(dto.payment())
                .build();

        deleteBasket(principalDetails.getUser(), orderItems);

        return orderRepository.save(order).getId();
    }

    public Page<OrderListResponseDto> getList(PrincipalDetails principalDetails, Pageable pageable) {
        return orderRepository.findAllByUserId(principalDetails.getUser().getId(), pageable).map(Order::toDtoList);
    }

    public OrderResponseDto get(Long orderId) {
        return findOrder(orderId).toDto();
    }


    public void cancel(Long orderId) {
        findOrder(orderId).cancel();
    }

    public List<BasketResponseDto> getBasketList(List<Long> itemIds) {
        return basketRepository.findAllByItemIdIn(itemIds).stream()
                .map(Basket::toDto).collect(Collectors.toList());
    }

    private Order findOrder(Long id) {
        return orderRepository.findByIdWithFetchJoinOrderItem(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );
    }

    private List<OrderItem> createOrderItems(OrderRequestDto dto) {
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequestDto orderItem : dto.orderItems()) {
            Item item = itemRepository.findByIdWithFetchJoinUser(orderItem.itemId()).orElseThrow(() ->
                    new CustomException(ErrorCode.ID_NOT_FOUND));

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

    private void deleteBasket(User user, List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            basketRepository.deleteByUserIdAndItemId(user.getId(), orderItem.getItem().getId());
        }
    }

}
