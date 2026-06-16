package br.com.alturionx.lead_recall_ai_backend.controller;

import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.model.Message;
import br.com.alturionx.lead_recall_ai_backend.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    public record IncomingMessageRequest(
            String phone,
            String content
    ) {}

    @PostMapping
    public Message receive(@RequestBody IncomingMessageRequest request) {

        return messageService.processIncomingMessage(
                request.phone(),
                request.content()
        );
    }
}