package com.personal.oldbookstore.domain.like.service;

import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.like.dto.LikeItemResponseDto;
import com.personal.oldbookstore.domain.like.entity.LikeItem;
import com.personal.oldbookstore.domain.like.repository.LikeItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class LikeItemService {

    private final LikeItemRepository likeItemRepository;
    private final ItemRepository itemRepository;

    public Page<LikeItemResponseDto> getList(Long id, Pageable pageable) {
        return likeItemRepository.findAllByUserId(id, pageable).map(LikeItem::toDto);
    }

    public void delete(User user, Long itemId) {
        Item item = getItem(itemId);
        item.decreaseLikeCount();

        likeItemRepository.deleteByUserIdAndItemId(user.getId(), itemId);
    }

    public Long create(User user, Long itemId) {
        if (user == null) {
            throw new CustomException(ErrorCode.ONLY_USER);
        }

        Item item = getItem(itemId);

        LikeItem likeItem = LikeItem.builder()
                .user(user)
                .item(item)
                .build();

        item.incrementLikeCount();

        return likeItemRepository.save(likeItem).getId();
    }

    private Item getItem(Long itemId) {
        return itemRepository.findByIdWithFetchJoinUser(itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND));
    }
}
