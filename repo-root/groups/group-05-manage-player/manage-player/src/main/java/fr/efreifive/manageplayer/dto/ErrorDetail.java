package fr.efreifive.manageplayer.dto;

public record ErrorDetail(
    String field,
    String issue
) {
}
