package Solo_Project.Library_API.member.controller;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.library.entity.Library;
import Solo_Project.Library_API.domain.library.service.LibraryService;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import Solo_Project.Library_API.domain.libraryMember.service.LibraryMemberService;
import Solo_Project.Library_API.domain.member.controller.MemberController;
import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.repository.MemberRepository;
import Solo_Project.Library_API.domain.member.service.MemberService;
import Solo_Project.Library_API.domain.memberBook.dto.MemberBookDto;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.mapper.MemberBookMapper;
import Solo_Project.Library_API.domain.memberBook.repository.MemberBookRepository;
import Solo_Project.Library_API.domain.memberBook.service.MemberBookService;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static Solo_Project.Library_API.util.ApiDocumentUtils.getRequestPreProcessor;
import static Solo_Project.Library_API.util.ApiDocumentUtils.getResponsePreProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;

import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MemberController.class)
@AutoConfigureRestDocs
@AutoConfigureMockMvc(addFilters = false)
public class MemberControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Gson gson;

    @MockBean
    private MemberRepository memberRepository;
    @MockBean
    private MemberBookRepository memberBookRepository;
    @MockBean
    private MemberBookService memberBookService;
    @MockBean
    private MemberMapper memberMapper;
    @MockBean
    private MemberService memberService;
    @MockBean
    private LibraryService libraryService;
    @MockBean
    private LibraryMemberRepository libraryMemberRepository;
    @MockBean
    private LibraryMemberService libraryMemberService;
    @MockBean
    private MemberBookMapper memberBookMapper;

//    @Test
//    public void postMemberTest() throws Exception {
//        Long libraryId = 1L;
//        MemberDto.Post post = new MemberDto.Post("David","010-1111-1111","han@han.com","1234");
//        given(memberMapper.memberDtoPostToMember(any(MemberDto.Post.class))).willReturn(new Member());
//        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());
//
//        MemberDto.Response response = new MemberDto.Response(1L,1L,post.getName(),
//                post.getPhone(),post.getEmail(),post.getPassword(),"http://localhost:8080/members/1");
//
//        given(memberMapper.memberToMemberDtoResponse(any(Member.class))).willReturn(response);
//
//        String content = gson.toJson(post);
//
//        ResultActions actions =
//                mockMvc.perform(
//                        RestDocumentationRequestBuilders.post("/members/{libraryId}",libraryId)
//                                .accept(MediaType.APPLICATION_JSON)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .content(content)
//                );
//        actions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value(post.getName()))
//                .andExpect(jsonPath("$.phone").value(post.getPhone()))
//                .andExpect(jsonPath("$.email").value(post.getEmail()))
//                .andExpect(jsonPath("$.password").value(post.getPassword()))
//                .andDo(document(
//                        "post-member",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        pathParameters(
//                                parameterWithName("libraryId").description("도서관 식별자")
//                        ),
//                        requestFields(
//                                List.of(
//                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
//                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
//                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
//                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
//                                )
//                        ),
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
//                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("도서관 식별자"),
//                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
//                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
//                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
//                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
//                                        fieldWithPath("url").type(JsonFieldType.STRING).description("조회 URL")
//                                )
//                        )
//                ));
//    }
//    @Test
//    public void getMemberTest() throws Exception {
//        Long libraryId = 1L;
//        Long memberId = 1L;
//
//        Member member = new Member(1L,"David","010-1111-1111","han@han.com","1234",1L);
//        given(memberService.findMember(Mockito.any(Long.class))).willReturn(member);
//
//        MemberDto.Response response = new MemberDto.Response(libraryId,memberId,member.getName(),
//                member.getPhone(),member.getEmail(),member.getPassword(),"http://localhost:8080/members/1");
//        given(memberMapper.memberToMemberDtoResponse(any(Member.class))).willReturn(response);
//
//        ResultActions actions =
//                mockMvc.perform(
//                        RestDocumentationRequestBuilders.get("/members/{libraryId}/{memberId}",libraryId,memberId)
//                                .accept(MediaType.APPLICATION_JSON)
//                );
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
//                .andExpect(jsonPath("$.name").value(response.getName()))
//                .andExpect(jsonPath("$.phone").value(response.getPhone()))
//                .andExpect(jsonPath("$.email").value(response.getEmail()))
//                .andExpect(jsonPath("$.password").value(response.getPassword()))
//                .andExpect(jsonPath("$.libraryId").value(response.getLibraryId()))
//                .andDo(document("get-member",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        pathParameters(
//                                parameterWithName("libraryId").description("도서관 식별자"),
//                                parameterWithName("memberId").description("회원 식별자")
//                        ),
//                        responseFields(
//                                List.of(
//                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("소속 도서관 식별자"),
//                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
//                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
//                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
//                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
//                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
//                                        fieldWithPath("url").type(JsonFieldType.STRING).description("조회 URL")
//                                )
//                        )
//                        ));
//    }
//    @Test
//    public void deleteMemberTest() throws Exception {
//        Long libraryId = 1L;
//        Long memberId = 1L;
//
//        LibraryMember libraryMember = new LibraryMember();
//        given(libraryMemberService.findByLibrary_IdAndMember_Id(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(libraryMember);
//        given(libraryMemberService.findByLibrary_IdAndMember_Id(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(new LibraryMember());
//        given(memberBookRepository.findByMember_Id(Mockito.any(Long.class))).willReturn(Collections.emptyList());
//
//        ResultActions actions =
//                mockMvc.perform(
//                        RestDocumentationRequestBuilders.delete("/members/{libraryId}/{memberId}",libraryId,memberId)
//                                .accept(MediaType.APPLICATION_JSON)
//                );
//        actions
//                .andExpect(status().isNoContent())
//                .andDo(document("delete-member",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        pathParameters(
//                                parameterWithName("libraryId").description("도서관 식별자"),
//                                parameterWithName("memberId").description("회원 식별자")
//                        )
//                ));
//
//        verify(memberBookRepository,times(1)).deleteByMember_Id(Mockito.any(Long.class));
//        verify(memberService,times(1)).deleteMember(Mockito.any(Long.class));
//
//    }
//    @Test
//    public void getRentalHistoryTest() throws Exception {
//        Long memberId = 1L;
//
//        MemberBook memberBook = new MemberBook();
//
//        List<MemberBook> memberBooks = new ArrayList<>();
//        memberBooks.add(memberBook);
//
//        Page<MemberBook> page = new PageImpl<>(memberBooks);
//        given(memberBookService.findMemberBooksByMemberId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class)))
//                .willReturn(page);
//
//        MemberBookDto.RentalHistoryResponse response = new MemberBookDto.RentalHistoryResponse();
//        response.setMemberId(1L);
//        response.setLibraryId(1L);
//        response.setBookId(1L);
//        response.setMemberBookId(1L);
//        response.setBookTitle("해리포터");
//        response.setCreatedAt(LocalDate.now());
//        response.setDueReturnDate(response.getCreatedAt().plusDays(14));
//        response.setReturnedDate(LocalDate.now());
//
//        List<MemberBookDto.RentalHistoryResponse> responses = new ArrayList<>();
//        responses.add(response);
//
//        given(memberBookMapper.HistoryMemberBooksToMemberBooksDtoResponse(Mockito.any(List.class))).willReturn(responses);
//
//        ResultActions actions =
//                mockMvc.perform(
//                        RestDocumentationRequestBuilders.get("/members/history/{memberId}",memberId)
//                                .param("page","1")
//                                .param("size","10")
//                                .accept(MediaType.APPLICATION_JSON)
//                );
//        actions
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.data[0].memberId").value(response.getMemberId()))
//                .andExpect(jsonPath("$.data[0].libraryId").value(response.getLibraryId()))
//                .andExpect(jsonPath("$.data[0].bookId").value(response.getBookId()))
//                .andExpect(jsonPath("$.data[0].memberBookId").value(response.getMemberBookId()))
//                .andExpect(jsonPath("$.data[0].bookTitle").value(response.getBookTitle()))
//                .andExpect(jsonPath("$.data[0].createdAt").value(response.getCreatedAt().toString()))
//                .andExpect(jsonPath("$.data[0].dueReturnDate").value(response.getDueReturnDate().toString()))
//                .andExpect(jsonPath("$.data[0].returnedDate").value(response.getReturnedDate().toString()))
//                .andDo(print())
//                .andDo(document("get-member_Rental_history",
//                        getRequestPreProcessor(),
//                        getResponsePreProcessor(),
//                        pathParameters(
//                                parameterWithName("memberId").description("회원 식별자")
//                        ),
//                        requestParameters(
//                                parameterWithName("page").description("내용이 나오는 페이지"),
//                                parameterWithName("size").description("한 페이지에 나오는 최대 갯수")
//                        )
//                ));
//    }
}
