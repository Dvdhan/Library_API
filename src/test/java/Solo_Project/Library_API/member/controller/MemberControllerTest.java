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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

// 1. postMember()
// 회원 등록이 되는지 주 목적
// 1. 타겟 메서드에서 파라미터로 받는 libraryId를 생성.
// 2. 회원 정보를 포함하여 Dto.post 객체 생성.
// 3. 타겟 메서드에서 mapper 를 사용하여 dto -> entity 변경하므로 given().willReturn() 사용
// 4. 타겟 메서드에서 memberService 를 사용하여 createMember() 메서드 실행했으므로
// given().willReturn() 메서드 사용
// 5. 본래는 회원 등록이 되는지 확인하는 것이므로 libraryMember 생성은 생략
// 6. 타겟 메서드에서 응답으로 entity -> response 사용하므로 Dto.Response 객체 생성하여 필드값 선언
// 7. given().willReturn() 사용하여 mapper 의 entity -> dto.response 사용.
// 8. 응답을 나타내는 ResultActions actions 선언해줌
// 9. actions (응답 데이터) 의 필드가 post 로 받은 (요청 데이터) 값과 같을거라는 예상 문법 사용.
    @Test
    public void postMemberTest() throws Exception {
        Long libraryId = 1L;
        MemberDto.Post post = new MemberDto.Post("David","010-1111-1111","han@han.com","1234");
        given(memberMapper.memberDtoPostToMember(any(MemberDto.Post.class))).willReturn(new Member());
        given(memberService.createMember(Mockito.any(Member.class))).willReturn(new Member());

        MemberDto.Response response = new MemberDto.Response(1L,1L,post.getName(),
                post.getPhone(),post.getEmail(),post.getPassword(),"http://localhost:8080/members/1");

        given(memberMapper.memberToMemberDtoResponse(any(Member.class))).willReturn(response);

        String content = gson.toJson(post);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/members/{libraryId}",libraryId)
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(content)
                );
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(post.getName()))
                .andExpect(jsonPath("$.phone").value(post.getPhone()))
                .andExpect(jsonPath("$.email").value(post.getEmail()))
                .andExpect(jsonPath("$.password").value(post.getPassword()))
                .andDo(document(
                        "post-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("도서관 식별자")
                        ),
                        requestFields(
                                List.of(
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호")
                                )
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("도서관 식별자"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("조회 URL")
                                )
                        )
                ));
    }
    // 2. getMember()
// 1. 단일 회원 을 조회하는 메서드
// 2. 파라미터로 libraryId, memberId 를 받으니 변수 생성
// 3. 새로운 Member 객체 생성
// 4. given().willReturn() 사용하여 타겟 메서드에서 사용한 memberService.findMember() 메서드 사용
// 5. 응답 데이터인 Dto.Response 를 생성하여 생성한 Member 객체와 같은 정보를 설정
// 6. 응답 데이터 ResultActions 설정해주고 요청보낼 주소와 HttpMethod 종류를 입력
// 7. 응답 데이터의 필드들이 생성한 response 객체의 필드와 같은지 테스트 진행
    @Test
    public void getMemberTest() throws Exception {
        Long libraryId = 1L;
        Long memberId = 1L;

        Member member = new Member(1L,"David","010-1111-1111","han@han.com","1234",1L);
        given(memberService.findMember(Mockito.any(Long.class))).willReturn(member);

        MemberDto.Response response = new MemberDto.Response(libraryId,memberId,member.getName(),
                member.getPhone(),member.getEmail(),member.getPassword(),"http://localhost:8080/members/1");
        given(memberMapper.memberToMemberDtoResponse(any(Member.class))).willReturn(response);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/members/{libraryId}/{memberId}",libraryId,memberId)
                                .accept(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.phone").value(response.getPhone()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.password").value(response.getPassword()))
                .andExpect(jsonPath("$.libraryId").value(response.getLibraryId()))
                .andDo(document("get-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("libraryId").description("도서관 식별자"),
                                parameterWithName("memberId").description("회원 식별자")
                        ),
                        responseFields(
                                List.of(
                                        fieldWithPath("libraryId").type(JsonFieldType.NUMBER).description("소속 도서관 식별자"),
                                        fieldWithPath("memberId").type(JsonFieldType.NUMBER).description("회원 식별자"),
                                        fieldWithPath("name").type(JsonFieldType.STRING).description("이름"),
                                        fieldWithPath("phone").type(JsonFieldType.STRING).description("휴대폰 번호"),
                                        fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                        fieldWithPath("password").type(JsonFieldType.STRING).description("비밀번호"),
                                        fieldWithPath("url").type(JsonFieldType.STRING).description("조회 URL")
                                )
                        )
                        ));
    }
// 3. deleteMember()
    @Test
    public void deleteMemberTest() throws Exception {
        Long libraryId = 1L;
        Long memberId = 1L;

// 1. 타겟 메서드에서 libraryMemberService 의 메서드를 통해 LibraryMember 객체를 만들고 있음
//libraryMember 객체를 생성하고 LibraryMember 엔티티에서 가지고 있는 Library,Member 객체를 생성하여 할당
// 타겟 메서드에서 사용한 libraryMemberService 의 메서드 동작을 given().willReturn() 으로 사용
// 해당 메서드가 제대로 동작하려면 미반납된 memberBook 이 없어야 하므로
// given().willReturn() 메서드에서 Collections.emptyList() 사용

        LibraryMember libraryMember = new LibraryMember();
        Library library = new Library();
        libraryMember.setLibrary(library);
        Member member = new Member();
        libraryMember.setMember(member);

        given(libraryMemberService.findByLibrary_IdAndMember_Id(Mockito.any(Long.class),Mockito.any(Long.class))).willReturn(libraryMember);
        given(memberBookRepository.findByMember_Id(Mockito.any(Long.class))).willReturn(Collections.emptyList());

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.delete("/members/{libraryId}/{memberId}",libraryId,memberId)
                                .accept(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isNoContent())
                .andDo(document("delete-member",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        // pathParameters() 메서드는 URL의 경로에 주어진 파라미터가 문서화되는데 사용되며,
                        // 이 매개변수가 무엇인지 설명하는 란이다.
                        pathParameters(
                                parameterWithName("libraryId").description("도서관 식별자"),
                                parameterWithName("memberId").description("회원 식별자")
                        )
                ));

        verify(memberBookRepository,times(1)).deleteByMember_Id(Mockito.any(Long.class));
        verify(memberService,times(1)).deleteMember(Mockito.any(Long.class));

    }
// 4. getRentalHistory()
    @Test
    public void getRentalHistoryTest() throws Exception {
// 회원이 대여기록 (memberBook)이 있는지 확인
// <대여 기록이 있다는 가정>
// 1. MemberBook 생성
// 2. MemberBook 엔티티가 가지고 있는 Member, Book 객체를 생성.
// 3. 생성한 Member, Book 객체를 MemberBook 에 할당.
// 4. 타겟 메서드에서 List<MemberBook> 객체를 Page<MemberBook> 으로부터 생성하므로
// List<MemberBook> 객체를 생성하고 memberBook 을 추가함.
// 5. 타겟 메서드처럼 Page<MemberBook> 를 생성하여 List<MemberBook> 객체를 전달함.
// 6. 타겟 메서드에서 Page<MemberBook> 객체를 memberBookService 의 메서드를 통해 생성했으므로
// 생성해둔 Page<MemberBook> 객체를 given().willReturn() 메서드에 사용.
// 7. 타겟 메서드에서 응답으로 List<response> 를 사용했으므로 단일 response 객체를 생성.
// 8. List<response> responses 생성하여 단일 response 를 추가해줌.
// 9. 생성한 List<response> responses 를 반환하기 위해
// 타겟 메서드에서 사용한 memberBookMapper 를 given().willReturn() 메서드로 사용.
// 10. Page 형태의 요청이므로 page, size 값을 param() 메서드를 사용하여 전달함.
        Long memberId = 1L;

        Member member = new Member();
        member.setMemberId(memberId);
        Book book = new Book();

        MemberBook memberBook = new MemberBook();
        memberBook.setMember(member);
        memberBook.setBook(book);

        List<MemberBook> memberBooks = new ArrayList<>();
        memberBooks.add(memberBook);

        Page<MemberBook> page = new PageImpl<>(memberBooks);
        given(memberBookService.findMemberBooksByMemberId(Mockito.any(Long.class),Mockito.any(Integer.class),Mockito.any(Integer.class)))
                .willReturn(page);

        MemberBookDto.RentalHistoryResponse response = new MemberBookDto.RentalHistoryResponse();
        response.setMemberId(1L);
        response.setLibraryId(1L);
        response.setBookId(1L);
        response.setMemberBookId(1L);
        response.setBookTitle("해리포터");
        response.setCreatedAt(LocalDate.now());
        response.setDueReturnDate(response.getCreatedAt().plusDays(14));
        response.setReturnedDate(LocalDate.now());

        List<MemberBookDto.RentalHistoryResponse> responses = new ArrayList<>();
        responses.add(response);
        given(memberBookMapper.HistoryMemberBooksToMemberBooksDtoResponse(Mockito.any(List.class))).willReturn(responses);

        ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.get("/members/history/{memberId}",memberId)
                                .param("page","1")
                                .param("size","10")
                                .accept(MediaType.APPLICATION_JSON)
                );
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].memberId").value(response.getMemberId()))
                .andExpect(jsonPath("$.data[0].libraryId").value(response.getLibraryId()))
                .andExpect(jsonPath("$.data[0].bookId").value(response.getBookId()))
                .andExpect(jsonPath("$.data[0].memberBookId").value(response.getMemberBookId()))
                .andExpect(jsonPath("$.data[0].bookTitle").value(response.getBookTitle()))
                .andExpect(jsonPath("$.data[0].createdAt").value(response.getCreatedAt().toString()))
                .andExpect(jsonPath("$.data[0].dueReturnDate").value(response.getDueReturnDate().toString()))
                .andExpect(jsonPath("$.data[0].returnedDate").value(response.getReturnedDate().toString()))
                .andDo(print())
                .andDo(document("get-member-history",
                        getRequestPreProcessor(),
                        getResponsePreProcessor(),
                        pathParameters(
                                parameterWithName("memberId").description("회원 식별자")
                        ),
                        requestParameters(
                                parameterWithName("page").description("내용이 나오는 페이지"),
                                parameterWithName("size").description("한 페이지에 나오는 최대 갯수")
                        )
                ));
    }
}
