package com.personal.oldbookstore.domain.item.service;

import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
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

    public ItemResponseDto get(Long itemId) {
        Item item = findItem(itemId);

        return item.toDto();
    }

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
    public void update(Long itemId, User user, ItemRequestDto dto) {
        Item item = findItem(itemId);

        if (!user.getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }

        item.updateItem(dto);
    }

    @Transactional
    public void delete(Long itemId, User user) {
        Item item = findItem(itemId);

        if (!user.getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.DELETE_ACCESS_DENIED);
        }

        itemRepository.delete(item);
    }

    private Item findItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND));
    }


}
