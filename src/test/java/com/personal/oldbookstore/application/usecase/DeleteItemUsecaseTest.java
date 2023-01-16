package com.personal.oldbookstore.application.usecase;

import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.item.service.ItemService;
import com.personal.oldbookstore.domain.order.dto.OrderItemRequestDto;
import com.personal.oldbookstore.domain.order.dto.OrderRequestDto;
import com.personal.oldbookstore.domain.order.entity.Payment;
import com.personal.oldbookstore.domain.order.service.OrderService;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class DeleteItemUsecaseTest {

    @Autowired
    private DeleteItemUsecase deleteItemUsecase;

    @Autowired
    private ItemService itemService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private List<OrderItemRequestDto> orderItemRequestDtos = new ArrayList<>();

    @BeforeEach
    void createUser() {
        user = saveUser("test@abc.com", "1234!@", "tester");
    }

    @Test
    @DisplayName("상품 삭제 실패 - 주문 상품")
    void deleteExistOrder() {
        //given
        ItemRequestDto itemRequestDto = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(user, itemRequestDto);

        OrderItemRequestDto orderItemDto = createOrderItemDto(itemId, 1);
        orderItemRequestDtos.add(orderItemDto);
        OrderRequestDto orderRequestDto = createOrderDto(orderItemRequestDtos, "tester", "01012345678", "CARD");
        orderService.create(user, orderRequestDto);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            deleteItemUsecase.excute(itemId, user);
        });
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void delete() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", "IT", "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        Long itemId = itemService.create(user, request);

        //when
        deleteItemUsecase.excute(itemId, user);

        //then
        assertThat(itemRepository.count()).isEqualTo(0);
    }

    private OrderItemRequestDto createOrderItemDto(Long itemId, Integer count) {
        return new OrderItemRequestDto(itemId, count);
    }

    private OrderRequestDto createOrderDto(List<OrderItemRequestDto> items, String recipient, String phone, String payment) {
        return OrderRequestDto.builder()
                .orderItems(items)
                .recipient(recipient)
                .phone(phone)
                .address(null)
                .payment(Payment.valueOf(payment))
                .build();
    }

    private User saveUser(String email, String password, String nickname) {
        return userRepository.save(User.JoinForm()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());
    }

    private ItemRequestDto createItem(String name, String category, String bookTitle, String bookAuthor,
                                      String contents, Integer stock, Integer price) {
        return ItemRequestDto.builder()
                .name(name)
                .category(Category.valueOf(category))
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .build();
    }
}