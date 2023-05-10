package Solo_Project.Library_API.domain.library.service;

import Solo_Project.Library_API.domain.library.entity.Library;
import Solo_Project.Library_API.domain.library.repository.LibraryRepository;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
public class LibraryService {
    private LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }
    public Library findLibrary (Long libraryId) {
        Library foundLibrary = libraryRepository.findByLibraryId(libraryId);
        return foundLibrary;
    }
}
