package Solo_Project.Library_API.domain.member.repository;

import Solo_Project.Library_API.domain.member.entity.Member;

public interface MemberRepositoryCustom {

    public Member findAMember(String name, String email);
}
