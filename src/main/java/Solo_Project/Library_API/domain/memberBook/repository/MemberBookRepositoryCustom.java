package Solo_Project.Library_API.domain.memberBook.repository;

import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;

import java.util.List;

public interface MemberBookRepositoryCustom {
    List<MemberBook> Q_DSL_findMemberBooks (Long memberId, String bookPublisher);
}
