package br.com.elibrary.application.book;

import br.com.elibrary.application.dto.response.BookCreatedResponse;
import br.com.elibrary.application.util.HttpHeaderUtil;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.RegisterBookService;
import br.com.elibrary.service.book.command.CreateBookCommand;
import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/books")
public class RegisterBookController {
    private final RegisterBookService registerBookService;

    public RegisterBookController(RegisterBookService registerBookService) {
        this.registerBookService = registerBookService;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<BookCreatedResponse> registerBook(@RequestBody @Valid CreateBookCommand createBookCommand) {
        Book book = registerBookService.register(createBookCommand);
        return ResponseEntity.created(HttpHeaderUtil.getLocationURI("/{id}", book.getId()))
                .body(BookCreatedResponse.convert(book));
    }
}