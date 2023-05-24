package Solo_Project.Library_API.domain.libraryMember.repository;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LibraryMemberRepositoryCustom {
    List<LibraryMember> Q_DSL_findEveryLibraryMembersByLibraryId(Long libraryId);
    LibraryMember Q_DSL_findLibraryMembersByLibrary_IdAndMember_Id(Long libraryId, Long memberId);
}
