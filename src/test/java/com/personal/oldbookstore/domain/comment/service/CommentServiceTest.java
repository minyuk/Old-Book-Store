package com.personal.oldbookstore.domain.comment.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.comment.dto.CommentRequestDto;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.comment.repository.CommentRepository;
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
class CommentServiceTest {

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ItemRepository itemRepository;

    private User user;
    private Item item;
    private PrincipalDetails principalDetails;

    @BeforeEach
    void createUserWithItem() {
        user = saveUser("test@abc.com", "1234!@", "tester");
        principalDetails = new PrincipalDetails(user);

        item = saveItem(user, "습관 만들어요", "DEVELOPMENT", "아주 작은 습관의 힘", "제임스 클리어", "미개봉 제품", 100, 6000);
    }

    @Test
    @DisplayName("댓글 등록 실패 - 존재하지 않는 상품")
    void createNotFound() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.create(principalDetails, 10L, request);
        });
    }

    @Test
    @DisplayName("댓글 등록 실패 - 비회원")
    void createOnlyUser() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.create(null, item.getId(), request);
        });
    }

    @Test
    @DisplayName("댓글 등록 성공")
    void create() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);

        //when
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        //then
        assertThat(commentRepository.count()).isEqualTo(1);
        assertThat(response.contents()).isEqualTo("책 상태가 궁금합니다.");
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