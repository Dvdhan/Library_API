package Solo_Project.Library_API.domain.memberBook.repository;

import Solo_Project.Library_API.domain.memberBook.entity.MemberBook;
import Solo_Project.Library_API.domain.memberBook.entity.QMemberBook;
import com.querydsl.jpa.impl.JPAQueryFactory;

import java.util.List;

public class MemberBookRepositoryImpl implements MemberBookRepositoryCustom{
    private final JPAQueryFactory jpaQueryFactory;
    private QMemberBook qMemberBook = QMemberBook.memberBook;

    public MemberBookRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<MemberBook> Q_DSL_findMemberBooks(Long memberId, String bookPublisher) {
        return jpaQueryFactory
                .selectFrom(qMemberBook)
                .where(qMemberBook.member.memberId.eq(memberId)
                        .and(qMemberBook.returnedAt.isNull())
                        .and(qMemberBook.book.bookPublisher.eq(bookPublisher)))
                .fetch();
    }
}
