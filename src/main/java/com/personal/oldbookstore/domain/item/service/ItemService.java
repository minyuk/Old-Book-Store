package com.personal.oldbookstore.domain.item.service;

import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public Long create(User user, ItemRequestDto dto) {
        Item item = Item.builder()
                .user(user)
                .name(dto.name())
                .category(dto.category())
                .bookTitle(dto.bookTitle())
                .bookAuthor(dto.bookAuthor())
                .contents(dto.contents())
                .stock(dto.stock())
                .price(dto.price())
                .build();

        return itemRepository.save(item).getId();
    }

    @Transactional
    public void update(Long id, ItemRequestDto dto) {
        Item item = itemRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND));

        item.updateItem(dto);
    }


}
