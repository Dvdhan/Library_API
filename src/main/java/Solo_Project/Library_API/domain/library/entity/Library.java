package Solo_Project.Library_API.domain.library.entity;

import Solo_Project.Library_API.domain.libraryBook.entity.LibraryBook;
import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Library {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long libraryId;

    @Column
    private String libraryName;

    @OneToMany(mappedBy = "library", cascade = CascadeType.ALL)
    private List<LibraryMember> libraryMembers = new ArrayList<>();

    @OneToMany(mappedBy = "library")
    private List<LibraryBook> libraryBooks = new ArrayList<>();
}
