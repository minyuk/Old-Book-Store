package com.personal.oldbookstore.domain.order.repository;

import com.personal.oldbookstore.domain.order.entity.Order;

import java.util.Optional;

public interface OrderRepositoryCustom {

    Optional<Order> findByIdWithFetchJoinOrderItem(Long id);
}
