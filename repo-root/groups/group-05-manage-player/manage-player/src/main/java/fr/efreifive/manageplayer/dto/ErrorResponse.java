package fr.efreifive.manageplayer.dto;

import java.util.List;

public record ErrorResponse(
    String code,
    String message,
    List<ErrorDetail> details
) {
}
