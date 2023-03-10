package com.personal.oldbookstore.domain.comment.service;

import com.personal.oldbookstore.config.auth.PrincipalDetails;
import com.personal.oldbookstore.domain.comment.dto.CommentMyPageResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentRequestDto;
import com.personal.oldbookstore.domain.comment.dto.CommentResponseDto;
import com.personal.oldbookstore.domain.comment.dto.CommentUpdateRequestDto;
import com.personal.oldbookstore.domain.comment.entity.Comment;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

        item = saveItem(user, "?????? ????????????", "DEVELOPMENT", "?????? ?????? ????????? ???", "????????? ?????????", "????????? ??????", 100, 6000);
    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ?????? ?????? - ?????????")
    void getMyListPage() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(1, 2);

        //when
        Page<CommentMyPageResponseDto> comments = commentService.getMyList(principalDetails, pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("??????????????? ?????? ????????? ?????? ??????")
    void getMyList() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<CommentMyPageResponseDto> comments = commentService.getMyList(principalDetails, pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(3);
    }

    @Test
    @DisplayName("?????? ?????? ????????? ?????? ??????")
    void getList() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "??? ????????? ???????????????.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        //when
        List<CommentResponseDto> comments = commentService.getList(item.getId());

        //then
        assertThat(comments.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ????????? ?????????")
    void deleteNotEqual() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        User user2 = saveUser("test1@abc.com", "11111234!@", "tester1234");
        PrincipalDetails principalDetails2 = new PrincipalDetails(user2);

        //when
        //then
        assertThrows(CustomException.class, () ->
                commentService.delete(principalDetails2, response.id())
        );
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ???????????? ?????? ??????")
    void deleteNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () ->
                commentService.delete(principalDetails, 0L)
        );
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ?????? ??????, ?????? ??????")
    void delete() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        //when
        commentService.delete(principalDetails, response.id());

        //then
        Comment comment = commentRepository.findById(response.id()).orElse(null);
        assertThat(comment.getContents()).isEqualTo("????????? ???????????????.");
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ????????? ?????????")
    void updateNotEqual() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("?????? ?????????");

        PrincipalDetails principalDetails2 = new PrincipalDetails(saveUser("tt@tt.com", "123", "tt"));

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.update(principalDetails2, response.id(), updateRequest);
        });
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ???????????? ?????? ??????")
    void updateNotFound() {
        //given
        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("?????? ?????????");

        //when
        //then
        assertThrows(CustomException.class, () ->
                commentService.update(principalDetails, 0L, updateRequest)
        );
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void update() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("?????? ?????????");

        //when
        commentService.update(principalDetails, response.id(), updateRequest);

        //then
        Comment comment = commentRepository.findById(response.id()).orElse(null);
        assertThat(comment.getContents()).isEqualTo("?????? ?????????");
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ???????????? ?????? ??????")
    void createNotFound() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.create(principalDetails, 0L, request);
        });
    }

    @Test
    @DisplayName("?????? ?????? ?????? - ?????????")
    void createOnlyUser() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.create(null, item.getId(), request);
        });
    }

    @Test
    @DisplayName("?????? ?????? ??????")
    void create() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "??? ????????? ???????????????.", null);

        //when
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        //then
        assertThat(response.contents()).isEqualTo("??? ????????? ???????????????.");
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