package br.com.alturionx.lead_recall_ai_backend.controller;

import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.MessageEvent;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final EventBus eventBus;

    public MessageController(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    public record IncomingMessageRequest(
            String phone,
            String content,
            String name) {
    }

    @PostMapping
    public void receive(@RequestBody IncomingMessageRequest request) {

        eventBus.publish(
                new MessageEvent(
                        request.phone(),
                        request.content(),
                        "whatsapp",
                        request.name()));
    }
}