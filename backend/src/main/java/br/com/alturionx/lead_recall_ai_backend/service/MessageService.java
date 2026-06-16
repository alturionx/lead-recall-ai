package br.com.alturionx.lead_recall_ai_backend.service;

import org.springframework.stereotype.Service;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Message;
import br.com.alturionx.lead_recall_ai_backend.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final LeadService leadService;

    public MessageService(MessageRepository messageRepository,
                          LeadService leadService) {
        this.messageRepository = messageRepository;
        this.leadService = leadService;
    }

    public Message processIncomingMessage(String phone, String content) {

        Lead lead = leadService.findOrCreateLead(phone);

        Message message = new Message();
        message.setContent(content);
        message.setDirection("INBOUND");
        message.setCreatedAt(LocalDateTime.now());
        message.setLead(lead);

        return messageRepository.save(message);
    }
}