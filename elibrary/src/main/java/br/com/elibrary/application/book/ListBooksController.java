package br.com.elibrary.application.book;

import br.com.elibrary.application.dto.list.ListResponse;
import br.com.elibrary.application.dto.response.book.BookResponse;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/books")
public class ListBooksController {
    private final BookRepository bookRepository;

    public ListBooksController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional(readOnly = true)
    public ResponseEntity<ListResponse<BookResponse>> listBooks() {
        List<Book> books = bookRepository.listAll();
        List<BookResponse> bookResponses = books.stream()
                .map(BookResponse::convert)
                .toList();
        return ResponseEntity.ok(new ListResponse<>(bookResponses));
    }
}