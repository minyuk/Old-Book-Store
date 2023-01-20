package com.personal.oldbookstore.domain.like.repository;

import com.personal.oldbookstore.domain.like.entity.LikeItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface LikeItemRepositoryCustom {

    Optional<LikeItem> findByIdWithFetchJoinItem(Long id);

    Page<LikeItem> findAllByUserId(Long id, Pageable pageable);

    Optional<LikeItem> findByUserIdAndItemId(Long userId, Long itemId);

}
