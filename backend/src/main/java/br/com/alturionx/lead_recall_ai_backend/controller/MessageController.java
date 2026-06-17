package br.com.alturionx.lead_recall_ai_backend.controller;

import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.adapter.MessageEventAdapter;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageEventAdapter messageEventAdapter;

    public MessageController(MessageEventAdapter messageEventAdapter) {
        this.messageEventAdapter = messageEventAdapter;
    }

    public record IncomingMessageRequest(
            String phone,
            String content
    ) {}

    @PostMapping
    public void receive(@RequestBody IncomingMessageRequest request) {

        messageEventAdapter.toEvent(
                request.phone(),
                request.content(),
                "whatsapp"
        );
    }
}