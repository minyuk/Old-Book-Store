package com.personal.oldbookstore.domain.item.service;

import com.personal.oldbookstore.domain.item.dto.ItemListResponseDto;
import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.dto.ItemResponseDto;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.order.repository.OrderRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemService {

    private final ItemRepository itemRepository;
    private final OrderRepository orderRepository;

    public Page<ItemListResponseDto> getList(Pageable pageable, String category, String keyword) {
        return itemRepository.findAllBySearchOption(pageable, category, keyword).map(Item::toDtoList);
    }

    public ItemResponseDto get(Long itemId) {
        Item item = findItem(itemId);

        item.incrementViewCount();

        return item.toDto();
    }

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

    public void update(Long itemId, User user, ItemRequestDto dto) {
        Item item = findItem(itemId);

        if (!user.getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.EDIT_ACCESS_DENIED);
        }

        item.updateItem(dto);
    }

    public void delete(Long itemId, User user) {
        Item item = findItem(itemId);

        if (!user.getEmail().equals(item.getUser().getEmail())) {
            throw new CustomException(ErrorCode.DELETE_ACCESS_DENIED);
        }

        if (!orderRepository.findAllByItemId(itemId).isEmpty()) {
            throw new CustomException(ErrorCode.DELETE_EXIST_ORDER);
        }

        itemRepository.delete(item);
    }

    private Item findItem(Long id) {
        return itemRepository.findByIdWithFetchJoinUser(id).orElseThrow(() ->
                new CustomException(ErrorCode.ID_NOT_FOUND)
        );
    }

}
