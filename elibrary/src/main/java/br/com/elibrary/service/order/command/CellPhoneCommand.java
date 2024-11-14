package br.com.elibrary.service.order.command;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Defines a cellphone request in order to comply to the following format: (0?xx) x?xxxx-xxxx
 * @param code The code area for the number
 * @param phoneNumber The phone number
 */
public record CellPhoneCommand(@NotNull(message = "code.must.not.be.null")
                               @Max(value = 100, message = "code.with.invalid.range")
                               @Min(value = 10, message = "code.with.invalid.range") Integer code,
                               @NotBlank(message = "phone.number.must.not.be.null")
                               @Pattern(regexp = "\\d{8,9}", message = "phone.with.invalid.format")
                               @Size(max = 9, message = "phone.number.surpasses.max.size") String phoneNumber) {
}