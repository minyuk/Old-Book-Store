package com.personal.oldbookstore.domain.order.repository;

import com.personal.oldbookstore.domain.order.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface OrderRepositoryCustom {

    Page<Order> findAll(Pageable pageable);
    List<Order> findAllByItemId(Long itemId);
    Optional<Order> findByIdWithFetchJoinOrderItem(Long id);
}
