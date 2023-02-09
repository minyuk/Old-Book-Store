package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ItemRepositoryCustom {

    Page<Item> findAllBySearchOption(Pageable pageable, String category, String keyword);

    List<Item> findAllByCategory(String category);

    Optional<Item> findByIdWithFetchJoinUser(Long id);

    Page<Item> findAllByUserId(Long userId, Pageable pageable);

}
