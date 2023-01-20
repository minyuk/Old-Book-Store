package com.personal.oldbookstore.domain.basket.repository;

import com.personal.oldbookstore.domain.basket.entity.Basket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BasketRepository extends JpaRepository<Basket, Long>, BasketRepositoryCustom {
}
