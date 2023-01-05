package com.personal.oldbookstore.service;

import com.personal.oldbookstore.domain.item.dto.ItemRequestDto;
import com.personal.oldbookstore.domain.item.entity.Category;
import com.personal.oldbookstore.domain.item.entity.Item;
import com.personal.oldbookstore.domain.item.repository.ItemRepository;
import com.personal.oldbookstore.domain.item.service.ItemService;
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

@SpringBootTest
@Transactional
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void createUser() {
        user = saveUser("test@abc.com", "1234!@", "tester");
    }

    @Test
    @DisplayName("상품 수정 실패 - 작성자 불일치")
    void updateNotEqualUser() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(user, request);

        User user2 = saveUser("test2@efg.com", "1111!!!", "tester2");
        ItemRequestDto update = createItem("상품명 수정합니다", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
           itemService.update(itemId, user2, update);
        });
    }

    @Test
    @DisplayName("상품 수정 실패 - 존재하지 않는 상품")
    void updateNotFound() {
        //given
        ItemRequestDto request = createItem("상품명 수정합니다", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.update(1L, user, request);
        });
    }

    @Test
    @DisplayName("상품 수정 성공")
    void update() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        Long itemId = itemService.create(user, request);

        ItemRequestDto update = createItem("상품명 수정합니다", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        itemService.update(itemId, user, update);

        //then
        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("상품명 수정합니다");
    }

    @Test
    @DisplayName("상품 삭제 실패 - 작성자 불일치")
    void deleteNotEqualUser() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);
        Long itemId = itemService.create(user, request);

        User user2 = saveUser("test2@efg.com", "1111!!!", "tester2");

        //when
        //then
        assertThrows(CustomException.class, () -> {
           itemService.delete(itemId, user2);
        });
    }

    @Test
    @DisplayName("상품 삭제 실패 - 존재하지 않는 상품")
    void deleteNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () -> {
            itemService.delete(1L, user);
        });
    }

    @Test
    @DisplayName("상품 삭제 성공")
    void delete() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        Long itemId = itemService.create(user, request);

        //when
        itemService.delete(itemId, user);

        //then
        assertThat(itemRepository.count()).isEqualTo(0);
    }

    @Test
    @DisplayName("상품 등록 성공")
    void create() {
        //given
        ItemRequestDto request = createItem("자바 팔아요", Category.IT, "Java의 정석", "남궁성",
                "깨끗해요", 2, 5000);

        //when
        Long itemId = itemService.create(user, request);

        //then
        assertThat(itemRepository.count()).isEqualTo(1);

        Item item = itemRepository.findById(itemId).orElse(null);
        assertThat(item.getName()).isEqualTo("자바 팔아요");
        assertThat(item.getPrice()).isEqualTo(5000);
    }

    private User saveUser(String email, String password, String nickname) {
        return userRepository.save(User.JoinForm()
                .email(email)
                .password(password)
                .nickname(nickname)
                .build());
    }

    private ItemRequestDto createItem(String name, Category category, String bookTitle, String bookAuthor,
                            String contents, Integer stock, Integer price) {
        return ItemRequestDto.builder()
                .name(name)
                .category(category)
                .bookTitle(bookTitle)
                .bookAuthor(bookAuthor)
                .contents(contents)
                .stock(stock)
                .price(price)
                .build();
    }
}
