package fr.efreifive.manageplayer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record UpdatePlayerRequest(
    @Size(max = 100) String firstName,
    @Size(max = 100) String lastName,
    @Email String email,
    @Pattern(regexp = "^\\+?[0-9\\s\\-().]{7,20}$") String phone,
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$") String birthDate,
    @Pattern(regexp = "^(homme|femme|non binaire|non spécifié)$") String gender,
    @Positive Double height
) {
}
