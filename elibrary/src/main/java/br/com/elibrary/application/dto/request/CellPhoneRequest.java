package br.com.elibrary.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Defines a cellphone request in order to comply to the following format: (xx(x)?) x?xxxx-xxxx
 * @param code The code area for the number
 * @param phoneNumber The phone number
 */
public record CellPhoneRequest(@NotBlank(message = "code.must.not.be.empty")
                               @Pattern(regexp = "\\d{2,3}", message = "code.with.invalid.format") String code,
                               @NotBlank(message = "phone.number.must.not.null")
                               @Pattern(regexp = "\\d{8,9}")
                               @Size(max = 9, message = "phone.number.surpasses.max.size") String phoneNumber) {
}