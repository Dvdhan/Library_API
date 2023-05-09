package Solo_Project.Library_API.global;

import Solo_Project.Library_API.domain.member.entity.Member;
import Solo_Project.Library_API.global.advice.BusinessLogicException;
import Solo_Project.Library_API.global.advice.ExceptionCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Optional;

@Component
public class BookUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    private final BookAuthorityUtils authorityUtils;

    public BookUserDetailsService(MemberRepository memberRepository, BookAuthorityUtils authorityUtils) {
        this.memberRepository = memberRepository;
        this.authorityUtils = authorityUtils;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Member> optionalMember = memberRepository.findByEmail(username);
        Member findMember = optionalMember.orElseThrow(
                ()->new BusinessLogicException(ExceptionCode.MEMBER_NOT_FOUND));
        return new BookUserDetails(findMember);
    }
    private final class BookUserDetails extends Member implements UserDetails {
        BookUserDetails(Member member) {
            setMemberId(member.getMemberId());
            setName(member.getName());
            setPhone(member.getPhone());
            setEmail(member.getEmail());
            setPassword(member.getPassword());
            setRoles(member.getRoles());
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return authorityUtils.createAuthorities(this.getRoles());
        }

        @Override
        public String getUsername() {
            return getEmail();
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }
}
