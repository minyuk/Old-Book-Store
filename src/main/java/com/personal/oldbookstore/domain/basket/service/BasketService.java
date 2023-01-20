package com.personal.oldbookstore.domain.basket.service;

import com.personal.oldbookstore.domain.basket.dto.BasketRequestDto;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.basket.repository.BasketRepository;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class BasketService {

    private final BasketRepository basketRepository;

    private final ItemRepository itemRepository;

    public Long create(User user, Long itemId, BasketRequestDto dto) {
        if (user == null) {
            throw new CustomException(ErrorCode.ONLY_USER);
        }

        if (basketRepository.findByUserIdAndItemId(user.getId(), itemId).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_SAVED_BASKET);
        }

        Basket basket = Basket.builder()
                .user(user)
                .item(findItem(itemId))
                .count(dto.count())
                .build();

        return basketRepository.save(basket).getId();

    }

    private Item findItem(Long itemId) {
        return itemRepository.findByIdWithFetchJoinUser(itemId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.ID_NOT_FOUND);
        });
    }


}
