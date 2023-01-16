package com.personal.oldbookstore.domain.order.repository;

import com.personal.oldbookstore.domain.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
