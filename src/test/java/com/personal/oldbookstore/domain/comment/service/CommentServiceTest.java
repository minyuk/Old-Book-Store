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
    @DisplayName("마이페이지 댓글 리스트 조회 성공 - 페이징")
    void getMyListPage() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(1, 2);

        //when
        Page<CommentMyPageResponseDto> comments = commentService.getMyList(principalDetails, pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("마이페이지 댓글 리스트 조회 성공")
    void getMyList() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<CommentMyPageResponseDto> comments = commentService.getMyList(principalDetails, pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(3);
    }

    @Test
    @DisplayName("상품 댓글 리스트 조회 성공 - 페이징")
    void getListPage() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(1, 2);

        //when
        Page<CommentResponseDto> comments = commentService.getList(item.getId(), pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(1);
    }

    @Test
    @DisplayName("상품 댓글 리스트 조회 성공")
    void getList() {
        //given
        CommentRequestDto request1 = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response1 = commentService.create(principalDetails, item.getId(), request1);
        CommentRequestDto request2 = new CommentRequestDto(2, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request2);
        CommentRequestDto request3 = new CommentRequestDto(3, "책 상태가 궁금합니다.", response1.id());
        commentService.create(principalDetails, item.getId(), request3);

        Pageable pageable = PageRequest.of(0, 10);

        //when
        Page<CommentResponseDto> comments = commentService.getList(item.getId(), pageable);

        //then
        assertThat(comments.get().count()).isEqualTo(3);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 작성자 불일치")
    void deleteNotEqual() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
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
    @DisplayName("댓글 삭제 실패 - 존재하지 않는 댓글")
    void deleteNotFound() {
        //given
        //when
        //then
        assertThrows(CustomException.class, () ->
                commentService.delete(principalDetails, 0L)
        );
    }

    @Test
    @DisplayName("댓글 삭제 성공 - 댓글 유지, 내용 변경")
    void delete() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        //when
        commentService.delete(principalDetails, response.id());

        //then
        Comment comment = commentRepository.findById(response.id()).orElse(null);
        assertThat(comment.getContents()).isEqualTo("삭제된 댓글입니다.");
    }

    @Test
    @DisplayName("댓글 수정 실패 - 작성자 불일치")
    void updateNotEqual() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("수정 테스트");

        PrincipalDetails principalDetails2 = new PrincipalDetails(saveUser("tt@tt.com", "123", "tt"));

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.update(principalDetails2, response.id(), updateRequest);
        });
    }

    @Test
    @DisplayName("댓글 수정 실패 - 존재하지 않는 댓글")
    void updateNotFound() {
        //given
        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("수정 테스트");

        //when
        //then
        assertThrows(CustomException.class, () ->
                commentService.update(principalDetails, 0L, updateRequest)
        );
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void update() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);
        CommentResponseDto response = commentService.create(principalDetails, item.getId(), request);

        CommentUpdateRequestDto updateRequest = new CommentUpdateRequestDto("수정 테스트");

        //when
        commentService.update(principalDetails, response.id(), updateRequest);

        //then
        Comment comment = commentRepository.findById(response.id()).orElse(null);
        assertThat(comment.getContents()).isEqualTo("수정 테스트");
    }

    @Test
    @DisplayName("댓글 등록 실패 - 존재하지 않는 상품")
    void createNotFound() {
        //given
        CommentRequestDto request = new CommentRequestDto(1, "책 상태가 궁금합니다.", null);

        //when
        //then
        assertThrows(CustomException.class, () -> {
            commentService.create(principalDetails, 0L, request);
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