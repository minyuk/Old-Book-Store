package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, Long> {
}
