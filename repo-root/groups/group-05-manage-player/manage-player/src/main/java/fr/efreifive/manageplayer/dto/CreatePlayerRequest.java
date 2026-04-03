package fr.efreifive.manageplayer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record CreatePlayerRequest(
    @NotBlank @Size(max = 100) String firstName,
    @NotBlank @Size(max = 100) String lastName,
    @NotBlank @Email String email,
    @NotBlank @Pattern(regexp = "^\\+?[0-9\\s\\-().]{7,20}$") String phone,
    @NotBlank @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$") String birthDate,
    @NotBlank @Pattern(regexp = "^(homme|femme|non binaire|non spécifié)$") String gender,
    @NotNull @Positive Double height
) {
}
