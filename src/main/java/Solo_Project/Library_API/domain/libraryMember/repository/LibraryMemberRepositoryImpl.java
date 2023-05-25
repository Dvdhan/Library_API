package Solo_Project.Library_API.domain.libraryMember.repository;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.entity.QLibraryMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class LibraryMemberRepositoryImpl implements LibraryMemberRepositoryCustom{
    private final JPAQueryFactory queryFactory;
    private QLibraryMember qLibraryMember = QLibraryMember.libraryMember;

    public LibraryMemberRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public List<LibraryMember> Q_DSL_findEveryLibraryMembersByLibraryId(Long libraryId) {
        return queryFactory
                .selectFrom(qLibraryMember)
                .where(qLibraryMember.library.libraryId.eq(libraryId))
                .fetch();
    }

    @Override
    public LibraryMember Q_DSL_findLibraryMembersByLibrary_IdAndMember_Id(Long libraryId, Long memberId) {
        return queryFactory
                .selectFrom(qLibraryMember)
                .where(qLibraryMember.library.libraryId.eq(libraryId)
                        .and(qLibraryMember.member.memberId.eq(memberId)))
                .fetchOne();
    }

    @Override
    public LibraryMember Q_DSL_findOne(Long memberId) {
        return queryFactory
                .selectFrom(qLibraryMember)
                .where(qLibraryMember.member.memberId.eq(memberId))
                .fetchOne();
    }
}
