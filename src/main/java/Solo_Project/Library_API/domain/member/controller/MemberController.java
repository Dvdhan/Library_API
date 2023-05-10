package Solo_Project.Library_API.domain.member.controller;

import Solo_Project.Library_API.domain.member.dto.MemberDto;
import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.mapper.MemberMapper;
import Solo_Project.Library_API.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/members")
@Slf4j
public class MemberController {

    private final String url = "http://localhost:8080/members/";

    private MemberMapper mapper;
    private MemberService memberService;

    public MemberController(MemberMapper mapper, MemberService memberService) {
        this.mapper = mapper;
        this.memberService = memberService;
    }
    @PostMapping("/{library-Id}")
    public ResponseEntity postMember(@PathVariable("library-Id")@Positive Long libraryId,
                                     @RequestBody @Valid MemberDto.Post post) throws Exception {
        Member member = mapper.memberDtoPostToMember(post);
        member.setLibraryId(libraryId);
        Member createdMember = memberService.createMember(member);
        MemberDto.Response response = mapper.memberToMemberDtoResponse(createdMember);
        response.setUrl(url + response.getMemberId());
        HttpHeaders headers = new HttpHeaders();
        headers.add("Access-Control-Expose-Headers","Authorization");
        return new ResponseEntity(response, headers, HttpStatus.CREATED);
    }
    @GetMapping("/{member-Id}")
    public ResponseEntity getMember(@PathVariable("member-Id")@Positive Long memberId) throws Exception {
        Member member = memberService.findMember(memberId);
        MemberDto.Response response = mapper.memberToMemberDtoResponse(member);
        response.setUrl(url+response.getMemberId());
        return new ResponseEntity(response, HttpStatus.OK);
    }
    @DeleteMapping("/{library-Id}")
    public ResponseEntity deleteMember(@PathVariable("library-Id")@Positive Long libraryId) throws Exception {
        Long memberId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Member findMember = memberService.findMember(memberId);
        // 찾은 findMember 객체에 bookId가 있는지 확인하고 있다면 거절, 없다면 삭제처리해야함.
        // book 엔티티 완성하고 다시 돌아오기
        return null;
    }
}
