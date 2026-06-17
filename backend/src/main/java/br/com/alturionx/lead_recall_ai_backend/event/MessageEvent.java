package br.com.alturionx.lead_recall_ai_backend.event;

import java.time.LocalDateTime;

public record MessageEvent(

        String eventType,
        String userId,
        String channel,
        String message,
        LocalDateTime timestamp

) {}