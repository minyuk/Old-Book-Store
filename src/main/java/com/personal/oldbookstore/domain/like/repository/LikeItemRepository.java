package com.personal.oldbookstore.domain.like.repository;

import com.personal.oldbookstore.domain.like.entity.LikeItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

public interface LikeItemRepository extends JpaRepository<LikeItem, Long>, LikeItemRepositoryCustom {

    @Modifying
    void deleteByUserIdAndItemId(Long userId, Long itemId);
}
