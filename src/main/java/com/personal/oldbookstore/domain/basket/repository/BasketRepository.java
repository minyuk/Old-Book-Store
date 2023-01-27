package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long>, BasketRepositoryCustom {
    @Modifying
    void deleteByUserIdAndItemId(Long userId, Long itemId);

    List<Basket> findAllByItemIdIn(List<Long> itemIdList);
}
