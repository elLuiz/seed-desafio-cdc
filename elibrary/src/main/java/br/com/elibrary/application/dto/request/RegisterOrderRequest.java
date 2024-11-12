package br.com.elibrary.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterOrderRequest(
    @NotBlank(message = "email.must.not.be.empty")
    @Email(message = "email.must.be.valid") String email,
    @NotBlank(message = "name.must.not.be.empty")
    @Size(max = 120, message = "name.surpasses.max.size") String name,
    @NotBlank(message = "document.must.not.be.empty")
    @Pattern(regexp = "\\d{11,14}", message = "document.with.invalid.format") String document,
    @NotBlank(message = "last.name.must.not.be.empty")
    @Size(max = 120, message = "last.name.surpasses.max.size") String lastName,
    @NotNull(message = "address.must.not.be.null") OrderAddressRequest address,
    @NotNull(message = "cellphone.must.not.be.null") CellPhoneRequest cellphone) {}