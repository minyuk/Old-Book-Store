package com.personal.oldbookstore.application.usecase;

import com.personal.oldbookstore.domain.item.service.ItemService;
import com.personal.oldbookstore.domain.order.service.OrderService;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.util.exception.CustomException;
import com.personal.oldbookstore.util.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DeleteItemUsecase {

    private final ItemService itemService;

    private final OrderService orderService;

    public void excute(Long itemId, User user) {
        if (orderService.findAllByItemId(itemId).isEmpty()) {
            itemService.delete(itemId, user);
        } else {
            throw new CustomException(ErrorCode.DELETE_EXIST_ORDER);
        }
    }
}
