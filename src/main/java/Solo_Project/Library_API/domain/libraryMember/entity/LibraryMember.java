package Solo_Project.Library_API.domain.libraryMember.entity;

import Solo_Project.Library_API.domain.library.entity.Library;
import Solo_Project.Library_API.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class LibraryMember {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long libraryMemberId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "libraryId")
    private Library library;
}
