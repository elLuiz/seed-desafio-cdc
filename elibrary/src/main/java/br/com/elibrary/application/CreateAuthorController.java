package br.com.elibrary.application;

import br.com.elibrary.application.dto.response.AuthorCreatedResponse;
import br.com.elibrary.model.entity.Author;
import br.com.elibrary.model.request.CreateAuthorRequest;
import br.com.elibrary.service.AuthorRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

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
    public ResponseEntity<AuthorCreatedResponse> create(@RequestBody @Valid CreateAuthorRequest createAuthorRequest) {
        Author author = createAuthorRequest.convert();
        authorRepository.add(author);
        return ResponseEntity.created(getLocation(author)).body(AuthorCreatedResponse.convert(author));
    }

    private static URI getLocation(Author author) {
        return ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(author.getId())
                .toUri();
    }
}