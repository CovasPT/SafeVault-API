package com.tiagocovas.safevaultapi.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateSecretRequest(
    @NotBlank(message = "O segredo não pode estar vazio")
    String content
) {}