package br.com.elibrary.service.order.command;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Defines a cellphone request in order to comply to the following format: (0?xx) x?xxxx-xxxx
 * @param code The code area for the number
 * @param phoneNumber The phone number
 */
public record CellPhoneCommand(@NotBlank(message = "code.must.not.be.empty")
                               @Pattern(regexp = "0?\\d{2}", message = "code.with.invalid.format") String code,
                               @NotBlank(message = "phone.number.must.not.be.null")
                               @Pattern(regexp = "\\d{8,9}", message = "phone.with.invalid.format")
                               @Size(max = 9, message = "phone.number.surpasses.max.size") String phoneNumber) {
}