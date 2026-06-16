package br.com.alturionx.lead_recall_ai_backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateLeadRequest(@NotBlank String phone, String name) {}