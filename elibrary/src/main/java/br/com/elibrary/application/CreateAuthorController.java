package br.com.elibrary.application;

import br.com.elibrary.model.request.CreateAuthorRequest;
import br.com.elibrary.application.dto.response.AuthorCreatedResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/authors")
@RestController
public class CreateAuthorController {
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AuthorCreatedResponse> create(@RequestBody @Valid CreateAuthorRequest createAuthorRequest) {
        return new ResponseEntity<>(new AuthorCreatedResponse(), HttpStatus.CREATED);
    }
}