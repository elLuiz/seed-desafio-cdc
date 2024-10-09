package br.com.elibrary.model.request;

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
    @Size(min = 1, max = 255, message = "name.with.invalid.size")
    private String name;
    @NotNull(message = "email.must.not.be.null")
    @Email(message = "invalid.email")
    private String email;
    @NotBlank(message = "description.must.not.be.null")
    @Size(min = 1, max = 400, message = "description.with.invalid.size")
    private String description;
}