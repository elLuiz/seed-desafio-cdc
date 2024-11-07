package br.com.elibrary.application.book;

import br.com.elibrary.application.dto.response.BookDetailsResponse;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import br.com.elibrary.service.exception.EntityNotFound;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class ViewBookController {
    private final BookRepository bookRepository;

    public ViewBookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @GetMapping(value = "/{id}", produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional(readOnly = true)
    public ResponseEntity<BookDetailsResponse> findById(@PathVariable("id") Long id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new EntityNotFound("book.not.found", Book.class));
        return ResponseEntity.ok(BookDetailsResponse.convert(book));
    }
}