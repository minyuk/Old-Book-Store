package com.personal.oldbookstore.domain.basket.service;


import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.basket.dto.BasketResponseDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private PrincipalDetails principalDetails;

    @BeforeEach
    void createUserWithItem() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);
    }

    @Test
    @DisplayName("장바구니 리스트 조회 성공 - 페이징")
    void getListPage() {
        //given
        Item item1 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        basketService.create(principalDetails, item1.getId(), 1);

        Item item2 = saveItem(user, "Test", "IT", "testing", "tester", "test", 100, 10000);
        basketService.create(principalDetails, item2.getId(), 1);

        Pageable pageable = PageRequest.of(1, 1);

        //when
        Page<BasketResponseDto> basketList = basketService.getList(principalDetails, pageable);

        //then
        assertThat(basketList.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("장바구니 리스트 조회 성공")
    void getList() {
        //given
        Item item1 = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        basketService.create(principalDetails, item1.getId(), 1);

        Item item2 = saveItem(user, "Test", "IT", "testing", "tester", "test", 100, 10000);
        basketService.create(principalDetails, item2.getId(), 1);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<BasketResponseDto> basketList = basketService.getList(principalDetails, pageable);

        //then
        assertThat(basketList.get().count()).isEqualTo(2);
    }

    @Test
    @DisplayName("장바구니 삭제 성공")
    void delete() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        Long basketId = basketService.create(principalDetails, item.getId(), 1);

        //when
        basketService.delete(user, item.getId());

        //then
        assertThat(basketRepository.findById(basketId).orElse(null)).isNull();
    }

    @Test
    @DisplayName("장바구니 수정 실패 - 존재하지 않는 상품")
    void updateNotFound() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        basketService.create(principalDetails, item.getId(), 1);


        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.update(principalDetails, 0L, 10);
        });
    }

    @Test
    @DisplayName("장바구니 수정 성공")
    void update() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        basketService.create(principalDetails, item.getId(), 1);

        //when
        basketService.update(principalDetails, item.getId(), 10);

        //then
        Basket basket = basketRepository.findByUserIdAndItemId(user.getId(), item.getId()).orElse(null);
        assertThat(basket.getCount()).isEqualTo(10);
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 이미 등록 된 상품")
    void createExistItem() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);
        basketService.create(principalDetails, item.getId(), 1);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(principalDetails, item.getId(), 1);
        });
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 비회원")
    void createOnlyUser() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(null, item.getId(), 1);
        });
    }

    @Test
    @DisplayName("장바구니 등록 실패 - 존재하지 않는 상품")
    void createNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            basketService.create(principalDetails, 0L, 1);
        });
    }

    @Test
    @DisplayName("장바구니 등록 성공")
    void create() {
        //given
        Item item = saveItem(user, "자바 팔아요", "IT", "Java의 정석", "남궁성", "깨끗해요", 100, 10000);

        //when
        basketService.create(principalDetails, item.getId(), 1);

        //then
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