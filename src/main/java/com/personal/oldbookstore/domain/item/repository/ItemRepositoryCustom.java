package com.personal.oldbookstore.domain.item.repository;

import com.personal.oldbookstore.domain.item.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ItemRepositoryCustom {

    Page<Item> findAllBySearchOption(Pageable pageable, String category, String keyword);
    Optional<Item> findByIdWithFetchJoinUser(Long id);

}