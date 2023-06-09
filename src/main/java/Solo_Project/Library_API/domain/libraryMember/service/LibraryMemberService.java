package Solo_Project.Library_API.domain.libraryMember.service;

import Solo_Project.Library_API.domain.libraryMember.entity.LibraryMember;
import Solo_Project.Library_API.domain.libraryMember.repository.LibraryMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LibraryMemberService {

    @Autowired
    private LibraryMemberRepository libraryMemberRepository;

    public Page<LibraryMember> findAllLibraryMembersByLibraryId(Long libraryId, int page, int size) {
        return libraryMemberRepository.findAllLibraryMembersByLibraryId(libraryId, PageRequest.of(page,size));
    }
//    public List<LibraryMember> findAllLibraryMembersByLibraryId(Long libraryId) {
//        return libraryMemberRepository.Q_DSL_findEveryLibraryMembersByLibraryId(libraryId);
//    }


    public LibraryMember findByLibrary_IdAndMember_Id(Long libraryId, Long memberId) {
        return libraryMemberRepository.findByLibrary_IdAndMember_Id(libraryId, memberId);
    }
}
