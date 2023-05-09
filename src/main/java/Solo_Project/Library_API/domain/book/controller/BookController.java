package Solo_Project.Library_API.domain.book.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Positive;

@RestController
@Validated
@RequestMapping("/books")
@Slf4j
public class BookController {

    private final String url = "http://localhost:8080/books/";

    @PostMapping("/{library-Id}/{book-Id}")
    public ResponseEntity postBook(@PathVariable("library-Id")@Positive Long libraryId,
                                   @PathVariable("book-Id")@Positive Long bookId) {
        return null;
    }

}
