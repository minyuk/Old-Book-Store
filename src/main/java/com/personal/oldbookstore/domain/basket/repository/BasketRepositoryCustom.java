package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BasketRepositoryCustom {

    Optional<Basket> findByUserIdAndItemId(Long userId, Long itemId);

    Page<Basket> findAllByUserId(Long id, Pageable pageable);

}
