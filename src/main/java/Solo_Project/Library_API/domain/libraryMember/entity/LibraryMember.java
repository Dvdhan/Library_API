package Solo_Project.Library_API.domain.libraryMember.entity;

import Solo_Project.Library_API.domain.library.entity.Library;
import Solo_Project.Library_API.domain.member.entity.Member;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class LibraryMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long libraryMemberId;

    @Column
    private Long overdueDays;

    @Column
    private LocalDate lastOverdueDate;

    @ManyToOne
    @JoinColumn(name = "memberId")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "libraryId")
    private Library library;

    public void setOverdueDays(Long overdueDays) {
        this.overdueDays = overdueDays;
        this.lastOverdueDate = LocalDate.now();
    }
}
