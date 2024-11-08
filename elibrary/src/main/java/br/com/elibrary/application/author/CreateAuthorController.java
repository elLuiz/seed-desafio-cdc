package br.com.elibrary.application.author;

import br.com.elibrary.application.dto.request.CreateAuthorRequest;
import br.com.elibrary.application.dto.response.author.AuthorCreatedResponse;
import br.com.elibrary.application.util.HttpHeaderUtil;
import br.com.elibrary.model.author.Author;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.util.log.StatefulRequestLogger;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/authors")
@RestController
public class CreateAuthorController {
    private final AuthorRepository authorRepository;

    @Autowired
    public CreateAuthorController(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Transactional
    @StatefulRequestLogger(action = "Create Author")
    public ResponseEntity<AuthorCreatedResponse> create(@RequestBody @Valid CreateAuthorRequest createAuthorRequest) {
        Author author = createAuthorRequest.convert();
        authorRepository.add(author);
        return ResponseEntity.created(HttpHeaderUtil.getLocationURI("/{id}", author.getId())).body(AuthorCreatedResponse.convert(author));
    }
}