package com.personal.oldbookstore.domain.basket.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.basket.repository.BasketRepository;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
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
public class BasketService {

    private final BasketRepository basketRepository;

    private final ItemRepository itemRepository;

    public Page<BasketResponseDto> getList(PrincipalDetails principalDetails, Pageable pageable) {
        return basketRepository.findAllByUserId(principalDetails.getUser().getId(), pageable).map(Basket::toDto);
    }

    public void delete(User user, Long itemId) {
        basketRepository.deleteByUserIdAndItemId(user.getId(), itemId);
    }

    public void update(PrincipalDetails principalDetails, Long itemId, Integer count) {
        Basket basket = basketRepository.findByUserIdAndItemId(principalDetails.getUser().getId(), itemId).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND));

        basket.updateCount(count);
    }

    public Long create(PrincipalDetails principalDetails, Long itemId, Integer count) {
        if (principalDetails == null) {
            throw new CustomException(ErrorCode.ONLY_USER);
        }

        if (basketRepository.findByUserIdAndItemId(principalDetails.getUser().getId(), itemId).isPresent()){
            throw new CustomException(ErrorCode.ALREADY_SAVED_BASKET);
        }

        Basket basket = Basket.builder()
                .user(principalDetails.getUser())
                .item(findItem(itemId))
                .count(count)
                .build();

        return basketRepository.save(basket).getId();

    }

    private Item findItem(Long itemId) {
        return itemRepository.findByIdWithFetchJoinUser(itemId).orElseThrow(() -> {
            throw new CustomException(ErrorCode.ID_NOT_FOUND);
        });
    }

}
