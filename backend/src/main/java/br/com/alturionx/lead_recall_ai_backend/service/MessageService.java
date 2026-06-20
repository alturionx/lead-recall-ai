package br.com.alturionx.lead_recall_ai_backend.service;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Message;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    private final LeadRepository leadRepository;

    public MessageService(
            MessageRepository messageRepository,
            LeadRepository leadRepository) {

        this.messageRepository = messageRepository;
        this.leadRepository = leadRepository;
    }

    /**
     * 📩 Responsabilidade:
     * - Garantir que o Lead exista
     * - Salvar Message vinculada ao Lead
     */
    public Message processIncomingMessage(String phone, String content) {

        Lead lead = getOrCreateLead(phone);

        Message message = new Message();
        message.setContent(content == null ? "" : content.trim());
        message.setDirection("INBOUND");
        message.setCreatedAt(LocalDateTime.now());
        message.setLead(lead);

        return messageRepository.save(message);
    }

    /**
     * 🆕 Obtém ou cria lead
     */
    public Lead getOrCreateLead(String phone) {

        return leadRepository.findByPhone(phone)
                .orElseGet(() -> {
                    Lead newLead = new Lead();
                    newLead.setPhone(phone);
                    return leadRepository.save(newLead);
                });
    }

    /**
     * 🔎 Busca lead existente
     */
    public Lead findLeadByPhone(String phone) {
        return leadRepository.findByPhone(phone).orElse(null);
    }

    /**
     * 💾 Salva alterações do lead
     */
    public Lead saveLead(Lead lead) {
        return leadRepository.save(lead);
    }

    /**
     * 🧠 Histórico completo do lead
     * (vamos usar para fornecer contexto à IA)
     */
    public List<Message> getLeadMessages(Lead lead) {

        if (lead == null) {
            return List.of();
        }

        return messageRepository.findByLeadOrderByCreatedAtAsc(lead);
    }

    /**
     * 🧠 Histórico por telefone
     */
    public List<Message> getLeadMessages(String phone) {

        Lead lead = findLeadByPhone(phone);

        if (lead == null) {
            return List.of();
        }

        return messageRepository.findByLeadOrderByCreatedAtAsc(lead);
    }
}