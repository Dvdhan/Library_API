package Solo_Project.Library_API.domain.member.entity;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberId;

    @Column
    private String name;

    @Column
    private String phone;

    @Column(updatable = false, unique = true)
    @Email
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private Long libraryId;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<LibraryMember> libraryMembers = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberBook> memberBooks = new ArrayList<>();
}
