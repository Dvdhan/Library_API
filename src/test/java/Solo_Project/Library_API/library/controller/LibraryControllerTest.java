package Solo_Project.Library_API.library.controller;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.library.controller.LibraryController;
import Solo_Project.Library_API.domain.library.entity.Library;
import Solo_Project.Library_API.domain.libraryBook.dto.LibraryBookDto;
import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryBook.mapper.LibraryBookMapper;
import Solo_Project.Library_API.domain.libraryBook.service.LibraryBookService;
import Solo_Project.Library_API.domain.libraryMember.dto.LibraryMemberDto;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.mapper.LibraryMemberMapper;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static Solo_Project.Library_API.util.ApiDocumentUtils.getRequestPreProcessor;
import static Solo_Project.Library_API.util.ApiDocumentUtils.getResponsePreProcessor;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;


@WebMvcTest(LibraryController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class LibraryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean
    private MemberService memberService;
    @MockBean
    private MemberMapper memberMapper;
    @MockBean
    private LibraryBookService libraryBookService;
    @MockBean
    private LibraryBookMapper libraryBookMapper;
    @MockBean
    private LibraryMemberService libraryMemberService;
    @MockBean
    private LibraryMemberMapper libraryMemberMapper;
    @MockBean
    private MemberBookRepository memberBookRepository;

    @Test
    public void getMembersTest() throws Exception {
        Long libraryId = 1L;
        Long libraryMemberId = 1L;
        Long memberId = 1L;

        Member member = new Member(memberId,"David","010-1111-1111","han@han.com","1234",libraryId);

        LibraryMember libraryMember = new LibraryMember();
        libraryMember.setLibraryMemberId(libraryMemberId);
        libraryMember.setMember(member);

        List<LibraryMember> libraryMembers = new ArrayList<>();
        libraryMembers.add(libraryMember);
        Page<LibraryMember> libraryMemberPage = new PageImpl<>(libraryMembers);
        given(libraryMemberService.findAllLibraryMembersByLibraryId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).willReturn(libraryMemberPage);

        LibraryMemberDto.Response response = new LibraryMemberDto.Response();
        response.setLibraryId(libraryId);
        response.setMemberId(1L);
        response.setLibraryMemberId(libraryMember.getLibraryMemberId());
        response.setName(libraryMember.getMember().getName());
        response.setPhone(libraryMember.getMember().getPhone());
        response.setEmail(libraryMember.getMember().getEmail());
        response.setUrl("http://localhost:8080/library/1/members");

        List<LibraryMemberDto.Response> responses = new ArrayList<>();
        responses.add(response);

        given(libraryMemberMapper.libraryMembersToLibraryMembersDtoResponse(Mockito.any(List.class))).willReturn(responses);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/library/{libraryId}/members",libraryId)
                                .param("page","1")
                                .param("size","10")
                                .accept(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].libraryId").value(response.getLibraryId()))
                .andExpect(jsonPath("$.data[0].memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.data[0].libraryMemberId").value(response.getLibraryMemberId()))
                .andExpect(jsonPath("$.data[0].name").value(response.getName()))
                .andExpect(jsonPath("$.data[0].phone").value(response.getPhone()))
                .andExpect(jsonPath("$.data[0].email").value(response.getEmail()))
                .andExpect(jsonPath("$.data[0].url").value(response.getUrl()))
                .andDo(document("get-library_members",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("대상 도서관 ID")
                        ),requestParameters(
                                parameterWithName("page").description("목록 페이지"),
                                parameterWithName("size").description("한 페이지에 표시되는 최대 갯수")
                        )
                        ));
    }
    @Test
    public void getBooksTest() throws Exception {
        Long libraryId = 1L;
        Long bookId = 1L;

        Book book = new Book(bookId,"해리포터","JK롤링","A출판사");

        LibraryBook libraryBook = new LibraryBook();
        libraryBook.setBookStatus(Book.BookStatus.AVAILABLE);
        libraryBook.setBook(book);
        Library library = new Library();
        library.setLibraryId(libraryId);
        libraryBook.setLibrary(library);

        List<LibraryBook> libraryBooks = new ArrayList<>();
        libraryBooks.add(libraryBook);

        Page<LibraryBook> libraryBookPage = new PageImpl<>(libraryBooks);
        given(libraryBookService.findAllLibraryBooksByLibraryId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class))).willReturn(libraryBookPage);

        LibraryBookDto.Response response = new LibraryBookDto.Response();
        response.setLibraryId(libraryBook.getLibrary().getLibraryId());
        response.setBookId(libraryBook.getBook().getBookId());
        response.setBookTitle(libraryBook.getBook().getBookTitle());
        response.setBookAuthor(libraryBook.getBook().getBookAuthor());
        response.setBookPublisher(libraryBook.getBook().getBookPublisher());
        response.setBookStatus(libraryBook.getBookStatus());

        List<LibraryBookDto.Response> responses = new ArrayList<>();
        responses.add(response);

        given(libraryBookMapper.libraryBooksToLibraryBooksDtoResponse(Mockito.any(List.class))).willReturn(responses);

        ResultActions actions
                =mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/library/{libraryId}/books",libraryId)
                                .param("page","1")
                                .param("size","10")
                                .accept(MediaType.APPLICATION_JSON)
        );
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].libraryId").value(response.getLibraryId()))
                .andExpect(jsonPath("$.data[0].bookId").value(response.getBookId()))
                .andExpect(jsonPath("$.data[0].libraryBookId").value(response.getLibraryBookId()))
                .andExpect(jsonPath("$.data[0].bookTitle").value(response.getBookTitle()))
                .andExpect(jsonPath("$.data[0].bookAuthor").value(response.getBookAuthor()))
                .andExpect(jsonPath("$.data[0].bookPublisher").value(response.getBookPublisher()))
                .andExpect(jsonPath("$.data[0].bookStatus").value(response.getBookStatus().toString()))
                .andExpect(jsonPath("$.data[0].url").value(response.getUrl()))
                .andDo(document(
                        "get-library_books",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("대상 도서관 ID")
                        ),
                        requestParameters(
                                parameterWithName("page").description("도서 목록 표시 페이지"),
                                parameterWithName("size").description("도서 목록 페이지당 최대 표시 수")
                        )
                ));
    }
}
