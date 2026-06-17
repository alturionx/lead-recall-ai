package br.com.alturionx.lead_recall_ai_backend.adapter;

import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.MessageEvent;

import java.time.LocalDateTime;

@Component
public class MessageEventAdapter {

    private final EventBus eventBus;

    public MessageEventAdapter(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public void toEvent(String phone, String message, String channel) {

        MessageEvent event = new MessageEvent(
                "message",
                phone,
                channel,
                message,
                LocalDateTime.now()
        );

        eventBus.publish(event);
    }
}