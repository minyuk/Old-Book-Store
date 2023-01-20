package com.personal.oldbookstore.domain.basket.service;


import com.personal.oldbookstore.domain.basket.dto.BasketRequestDto;
import com.personal.oldbookstore.domain.basket.entity.Basket;
import com.personal.oldbookstore.domain.basket.repository.BasketRepository;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.user.entity.User;
import com.personal.oldbookstore.domain.user.repository.UserRepository;
import com.personal.oldbookstore.util.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
class BasketServiceTest {

    @Autowired
    private BasketService basketService;

    @Autowired
    private BasketRepository basketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;

    @BeforeEach
    void createUserWithItem() {
        user = saveUser("test@abc.com", "1234!@", "tester");
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 이미 등록 된 상품")
    void createExistItem() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        BasketRequestDto dto = new BasketRequestDto(1);
        basketService.create(user, item.getId(), dto);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(user, item.getId(), dto);
        });
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 비회원")
    void createOnlyUser() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        BasketRequestDto dto = new BasketRequestDto(1);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(null, item.getId(), dto);
        });
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 존재하지 않는 상품")
    void createNotFound() {
        //given
        BasketRequestDto dto = new BasketRequestDto(1);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(user, 1L, dto);
        });
    }

    @Test
    @DisplayName("장바구니 등록 성공")
    void create() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        BasketRequestDto dto = new BasketRequestDto(1);

        //when
        basketService.create(user, item.getId(), dto);

        //then
        assertThat(basketRepository.count()).isEqualTo(1);

        Basket basket = basketRepository.findByUserIdAndItemId(user.getId(), item.getId()).orElse(null);
        assertThat(basket.getItem().getBookTitle()).isEqualTo("Java의 정석");
    }

    private Item saveItem(User user, String name, String category, String bookTitle, String bookAuthor,
                          String contents, Integer stock, Integer price) {
        return itemRepository.save(Item.builder()
                .user(user)
                .name(name)
                .category(Category.valueOf(category))
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .build());
    }

    private User saveUser(String email, String password, String nickname) {
        return userRepository.save(User.JoinForm()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());
    }
}