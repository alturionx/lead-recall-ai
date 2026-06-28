package br.com.alturionx.lead_recall_ai_backend.event;

public record MessageEvent(
        String phone,
        String message,
        String channel,
        String name
) implements Event {}