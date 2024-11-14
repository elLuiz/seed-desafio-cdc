package br.com.elibrary.service.order.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record OrderAddressCommand(
    @NotBlank(message = "address.must.not.be.empty")
    @Size(max = 200, message = "address.surpasses.max.size") String address,
    @NotBlank(message = "complement.must.not.be.empty")
    @Size(max = 200, message = "complement.surpasses.max.size") String complement,
    @NotBlank(message = "city.must.not.be.empty")
    @Size(max = 150, message = "city.surpasses.max.size") String city,
    @NotBlank(message = "zip.code.must.not.be.empty")
    @Size(max = 20, message = "zip.code.surpasses.max.size") String zipCode,
    @NotNull(message = "country.must.not.be.null") Long country,
    @Size(max = 150, message = "state.surpasses.max.size") String state) {}