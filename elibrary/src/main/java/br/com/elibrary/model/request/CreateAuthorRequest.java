package br.com.elibrary.model.request;

import br.com.elibrary.application.validation.UniqueEmail;
import br.com.elibrary.model.entity.Author;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAuthorRequest {
    @NotBlank(message = "name.must.not.be.null")
    @Size(max = 255, message = "name.with.invalid.size")
    private String name;
    @NotNull(message = "email.must.not.be.null")
    @Email(message = "invalid.email")
    @UniqueEmail
    private String email;
    @NotBlank(message = "description.must.not.be.null")
    @Size(max = 400, message = "description.with.invalid.size")
    private String description;

    public Author convert() {
        return new Author(this.name, this.email, this.description);
    }
}