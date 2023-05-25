package Solo_Project.Library_API.domain.member.repository;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.domain.member.entity.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;

public class MemberRepositoryImpl implements MemberRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    private QMember qMember = QMember.member;

    public MemberRepositoryImpl (JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Member findAMember(String name, String email) {
        return queryFactory
                .selectFrom(qMember)
                .where(qMember.name.eq(name).and(qMember.email.eq(email)))
                .fetchOne();
    }
}
