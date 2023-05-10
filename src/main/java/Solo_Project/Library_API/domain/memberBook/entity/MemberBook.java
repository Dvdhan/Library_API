package Solo_Project.Library_API.domain.memberBook.entity;

import Solo_Project.Library_API.domain.book.entity.Book;
import Solo_Project.Library_API.domain.member.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class MemberBook {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long memberBookId;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime returnedAt;

    @Column
    private Long libraryId;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "bookId")
    private Book book;
}
