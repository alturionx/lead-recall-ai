package br.com.alturionx.lead_recall_ai_backend.service;

import org.springframework.stereotype.Service;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Message;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.MessageRepository;

import java.time.LocalDateTime;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final LeadRepository leadRepository;

    public MessageService(MessageRepository messageRepository,
                          LeadRepository leadRepository) {
        this.messageRepository = messageRepository;
        this.leadRepository = leadRepository;
    }

    /**
     * 📩 Responsabilidade:
     * - Garantir Lead existe (SEM salvar novo vazio)
     * - Salvar Message vinculada ao Lead
     */
    public Message processIncomingMessage(String phone, String content) {

        Lead lead = leadRepository.findByPhone(phone)
                .orElseGet(() -> createTransientLead(phone));

        Message message = new Message();
        message.setContent(content);
        message.setDirection("INBOUND");
        message.setCreatedAt(LocalDateTime.now());
        message.setLead(lead);

        return messageRepository.save(message);
    }

    /**
     * ⚠️ IMPORTANTE:
     * NÃO salva no banco aqui
     */
    private Lead createTransientLead(String phone) {
        Lead lead = new Lead();
        lead.setPhone(phone);
        return lead;
    }

    /**
     * 🔎 Busca lead existente
     */
    public Lead findLeadByPhone(String phone) {
        return leadRepository.findByPhone(phone).orElse(null);
    }

    /**
     * 💾 ÚNICO ponto correto de persistência
     */
    public Lead saveLead(Lead lead) {
        return leadRepository.save(lead);
    }
}