package Solo_Project.Library_API.book.controller;

import Solo_Project.Library_API.domain.book.controller.BookController;
import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.mapper.LibraryBookMapper;
import Solo_Project.Library_API.domain.libraryBook.repository.LibraryBookRepository;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.controller.MemberController;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.dto.MemberBookDto;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.mapper.MemberBookMapper;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.domain.memberBook.service.MemberBookService;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static Solo_Project.Library_API.util.ApiDocumentUtils.getRequestPreProcessor;
import static Solo_Project.Library_API.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean
    private LibraryMemberRepository libraryMemberRepository;

    @MockBean
    private LibraryBookRepository libraryBookRepository;

    @MockBean
    private MemberService memberService;

    @MockBean
    private MemberBookRepository memberBookRepository;

    @MockBean
    private LibraryBookService libraryBookService;

    @MockBean
    private MemberBookService memberBookService;

    @MockBean
    private LibraryMemberService libraryMemberService;

    @MockBean
    private MemberBookMapper memberBookMapper;

    @MockBean
    private LibraryBookMapper libraryBookMapper;

    @Test
    public void postBookRentalTest() throws Exception {
        Long libraryId = 1L;
        Long bookId = 1L;
        Long memberId = 1L;

        LibraryMember libraryMember = new LibraryMember();
        given(libraryMemberService.findByLibrary_IdAndMember_Id(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(libraryMember);

        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        given(libraryBookService.findLibraryBookByLibraryIdBookId(Mockito.any(Long.class), Mockito.any(Long.class))).willReturn(libraryBook);

        Member member = new Member();
        given(memberService.findMember(Mockito.any(Long.class))).willReturn(member);

        given(memberBookRepository.countByMemberAndReturnedAtIsNull(member)).willReturn(4);

        MemberBook memberBook = new MemberBook();
        memberBook.setLibraryId(libraryId);
        memberBook.setMember(member);
        memberBook.setBook(libraryBook.getBook());
        memberBook.setCreatedAt(LocalDate.now());
        memberBook.setDueReturn(LocalDate.now().plusDays(14));
        given(memberBookRepository.save(Mockito.any(MemberBook.class))).willReturn(memberBook);

        MemberBookDto.Response response = new MemberBookDto.Response();
        response.setLibraryId(libraryId);
        response.setMemberId(memberId);
        response.setBookId(bookId);
        response.setMemberBookId(1L);
        response.setBookTitle("해리포터");
        response.setCreatedAt(LocalDate.now());
        response.setDueReturnDate(LocalDate.now().plusDays(14));
        given(memberBookMapper.memberBookToMemberBookDtoResponse(Mockito.any(MemberBook.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/books/{libraryId}/{bookId}/{memberId}",libraryId,bookId,memberId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.libraryId").value(response.getLibraryId()))
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.bookId").value(response.getBookId()))
                .andExpect(jsonPath("$.memberBookId").value(response.getMemberBookId()))
                .andExpect(jsonPath("$.bookTitle").value(response.getBookTitle()))
                .andExpect(jsonPath("$.createdAt").value(response.getCreatedAt().toString()))
                .andExpect(jsonPath("$.dueReturnDate").value(response.getDueReturnDate().toString()))
                .andDo(document(
                        "post-bookRental",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("대여하고자 하는 LibraryId"),
                                parameterWithName("bookId").description("대여하고자 하는 도서관의 BookId"),
                                parameterWithName("memberId").description("대여하는 도서관의 memberId")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("대여한 LibraryId"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("대여 당사자 memberId"),
                                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("대여한 bookId"),
                                        fieldWithPath("memberBookId").type(JsonFieldType.NUMBER).description("대여된 memberBookId"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("대여한 책 이름"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대여한 날짜"),
                                        fieldWithPath("dueReturnDate").type(JsonFieldType.STRING).description("최대 반납일")
                                )
                        )
                ));
    }
    @Test
    public void deleteBookRentalTest() throws Exception {
        Long libraryId = 1L;
        Long bookId = 1L;
        Long memberId = 1L;
        Long memberBookId = 1L;

        LibraryBook libraryBook1 = new LibraryBook();
        libraryBook1.setBookStatus(Book.BookStatus.UNAVAILABLE);

        LibraryBook libraryBook2 = new LibraryBook();
        libraryBook2.setBookStatus(Book.BookStatus.UNAVAILABLE);

        given(libraryBookService.findLibraryBookByLibraryIdBookId(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(libraryBook1, libraryBook2);


        MemberBook memberBook1 = new MemberBook();
        memberBook1.setCreatedAt(LocalDate.now());
        memberBook1.setOverdueDays(0L);

        MemberBook memberBook2 = new MemberBook();
        memberBook2.setCreatedAt(LocalDate.now());
        memberBook2.setOverdueDays(1L);

        given(memberBookService.findMemberBookByMemberIdBookId(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(memberBook1, memberBook2);


        LibraryMember libraryMemberHasNoOverdueDays = new LibraryMember();
        LibraryMember libraryMemberHasOverdueDays = new LibraryMember();

        given(libraryMemberService.findByLibrary_IdAndMember_Id(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(libraryMemberHasNoOverdueDays, libraryMemberHasOverdueDays);


        MemberBookDto.ReturnResponse responseHasNoOverdueDays = new MemberBookDto.ReturnResponse();
        responseHasNoOverdueDays.setMemberId(memberId);
        responseHasNoOverdueDays.setLibraryId(libraryId);
        responseHasNoOverdueDays.setBookId(bookId);
        responseHasNoOverdueDays.setMemberBookId(memberBookId);
        responseHasNoOverdueDays.setBookTitle("해리포터1");
        responseHasNoOverdueDays.setCreatedAt(LocalDate.now());
        responseHasNoOverdueDays.setReturnedAt(LocalDate.now());
        responseHasNoOverdueDays.setOverdueDays(memberBook1.getOverdueDays());
        responseHasNoOverdueDays.setMessage("반납이 완료되었습니다");

        MemberBookDto.ReturnResponse responseHasOverdueDays = new MemberBookDto.ReturnResponse();
        responseHasOverdueDays.setMemberId(memberId);
        responseHasOverdueDays.setLibraryId(libraryId);
        responseHasOverdueDays.setBookId(bookId);
        responseHasOverdueDays.setMemberBookId(memberBookId);
        responseHasOverdueDays.setBookTitle("해리포터2");
        responseHasOverdueDays.setCreatedAt(LocalDate.now().minusDays(15));
        responseHasOverdueDays.setReturnedAt(LocalDate.now());
        responseHasOverdueDays.setOverdueDays(memberBook2.getOverdueDays());
        String availableDate = responseHasOverdueDays.getReturnedAt().plusDays(responseHasOverdueDays.getOverdueDays()).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        responseHasOverdueDays.setMessage("연체가 발생되어 "+responseHasOverdueDays.getOverdueDays()+"일 동안 대여할 수 없습니다. " +
                availableDate+" 일부터 대여할 수 있습니다.");;

        given(memberBookMapper.ReturnMemberBookToMemberBookDtoResponse(Mockito.any(MemberBook.class))).willReturn(responseHasNoOverdueDays, responseHasOverdueDays);

        ResultActions actions1 =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/books/{libraryId}/{bookId}/{memberId}",libraryId,bookId,memberId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions1
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(responseHasNoOverdueDays.getMemberId()))
                .andExpect(jsonPath("$.libraryId").value(responseHasNoOverdueDays.getLibraryId()))
                .andExpect(jsonPath("$.bookId").value(responseHasNoOverdueDays.getBookId()))
                .andExpect(jsonPath("$.memberBookId").value(responseHasNoOverdueDays.getMemberBookId()))
                .andExpect(jsonPath("$.bookTitle").value(responseHasNoOverdueDays.getBookTitle()))
                .andExpect(jsonPath("$.createdAt").value(responseHasNoOverdueDays.getCreatedAt().toString()))
                .andExpect(jsonPath("$.returnedAt").value(responseHasNoOverdueDays.getReturnedAt().toString()))
                .andExpect(jsonPath("$.overdueDays").value(responseHasNoOverdueDays.getOverdueDays()))
                .andExpect(jsonPath("$.message").value(responseHasNoOverdueDays.getMessage()))
                .andDo(document(
                        "delete-BookRental",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("대여하고자 하는 LibraryId"),
                                parameterWithName("bookId").description("대여하고자 하는 도서관의 BookId"),
                                parameterWithName("memberId").description("대여하는 도서관의 memberId")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("반납한 회원 식별자"),
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("반납한 도서관 식별자"),
                                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("반납한 도서 식별자"),
                                        fieldWithPath("memberBookId").type(JsonFieldType.NUMBER).description("반납한 대여책 식별자"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대여한 책의 대여날짜"),
                                        fieldWithPath("returnedAt").type(JsonFieldType.STRING).description("대여한 책의 반납날짜"),
                                        fieldWithPath("overdueDays").type(JsonFieldType.NUMBER).description("연체 날짜"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("반납 상태 메세지")
                                )
                        )
                ));

        ResultActions actions2 =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/books/{libraryId}/{bookId}/{memberId}",2L,2L,2L)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions2
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(responseHasOverdueDays.getMemberId()))
                .andExpect(jsonPath("$.libraryId").value(responseHasOverdueDays.getLibraryId()))
                .andExpect(jsonPath("$.bookId").value(responseHasOverdueDays.getBookId()))
                .andExpect(jsonPath("$.memberBookId").value(responseHasOverdueDays.getMemberBookId()))
                .andExpect(jsonPath("$.bookTitle").value(responseHasOverdueDays.getBookTitle()))
                .andExpect(jsonPath("$.createdAt").value(responseHasOverdueDays.getCreatedAt().toString()))
                .andExpect(jsonPath("$.returnedAt").value(responseHasOverdueDays.getReturnedAt().toString()))
                .andExpect(jsonPath("$.overdueDays").value(responseHasOverdueDays.getOverdueDays()))
                .andExpect(jsonPath("$.message").value(responseHasOverdueDays.getMessage()))
                .andDo(document(
                        "delete-BookRental",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("대여하고자 하는 LibraryId"),
                                parameterWithName("bookId").description("대여하고자 하는 도서관의 BookId"),
                                parameterWithName("memberId").description("대여하는 도서관의 memberId")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("반납한 회원 식별자"),
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("반납한 도서관 식별자"),
                                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("반납한 도서 식별자"),
                                        fieldWithPath("memberBookId").type(JsonFieldType.NUMBER).description("반납한 대여책 식별자"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("책 제목"),
                                        fieldWithPath("createdAt").type(JsonFieldType.STRING).description("대여한 책의 대여날짜"),
                                        fieldWithPath("returnedAt").type(JsonFieldType.STRING).description("대여한 책의 반납날짜"),
                                        fieldWithPath("overdueDays").type(JsonFieldType.NUMBER).description("연체 날짜"),
                                        fieldWithPath("message").type(JsonFieldType.STRING).description("반납 상태 메세지")
                                )
                        )
                ));
    }
    @Test
    public void findBookTest() throws Exception {
        Long libraryId = 1L;
        Long bookId = 1L;

        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        given(libraryBookService.findLibraryBookByLibraryIdBookId(libraryId,bookId)).willReturn(libraryBook);

        LibraryBookDto.Response response = new LibraryBookDto.Response();
        response.setLibraryId(libraryId);
        response.setBookId(bookId);
        response.setLibraryBookId(1L);
        response.setBookTitle("해리포터");
        response.setBookAuthor("JK롤링");
        response.setBookPublisher("A출판사");
        response.setBookStatus(Book.BookStatus.AVAILABLE);
        response.setUrl("http://localhost:8080/books/1/1");

        given(libraryBookMapper.libraryBookToLibraryBookDtoResponse(Mockito.any(LibraryBook.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/books/{libraryId}/{bookId}",libraryId,bookId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.libraryId").value(response.getLibraryId()))
                .andExpect(jsonPath("$.bookId").value(response.getBookId()))
                .andExpect(jsonPath("$.libraryBookId").value(response.getLibraryBookId()))
                .andExpect(jsonPath("$.bookTitle").value(response.getBookTitle()))
                .andExpect(jsonPath("$.bookAuthor").value(response.getBookAuthor()))
                .andExpect(jsonPath("$.bookPublisher").value(response.getBookPublisher()))
                .andExpect(jsonPath("$.bookStatus").value(response.getBookStatus().toString()))
                .andExpect(jsonPath("$.url").value(response.getUrl()))
                .andDo(document(
                        "find-book",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("찾고자하는 도서관 식별자"),
                                parameterWithName("bookId").description("찾고자하는 도서 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("도서관 식별자"),
                                        fieldWithPath("bookId").type(JsonFieldType.NUMBER).description("도서 식별자"),
                                        fieldWithPath("libraryBookId").type(JsonFieldType.NUMBER).description("도서관에 보관된 도서 식별자"),
                                        fieldWithPath("bookTitle").type(JsonFieldType.STRING).description("도서 제목"),
                                        fieldWithPath("bookAuthor").type(JsonFieldType.STRING).description("도서 작가"),
                                        fieldWithPath("bookPublisher").type(JsonFieldType.STRING).description("도서 출판사"),
                                        fieldWithPath("bookStatus").type(JsonFieldType.STRING).description("도서 상태"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("도서 조회 url")
                                )
                        )
                ));
    }
}
