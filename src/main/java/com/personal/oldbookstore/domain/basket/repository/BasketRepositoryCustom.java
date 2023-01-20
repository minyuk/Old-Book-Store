package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;

import java.util.Optional;

public interface BasketRepositoryCustom {

    Optional<Basket> findByUserIdAndItemId(Long userId, Long itemId);

}
