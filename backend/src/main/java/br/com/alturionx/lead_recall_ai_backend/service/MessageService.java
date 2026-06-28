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
     * - Atualizar última interação
     * - Salvar Message vinculada ao Lead
     */
    public Message processIncomingMessage(String phone, String content, String name) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone não pode ser vazio");
        }

        LocalDateTime now = LocalDateTime.now();

        Lead lead = getOrCreateLead(phone);

        // Atualiza atividade recente
        lead.setLastInteractionAt(now);
        lead.setName(name);
        leadRepository.save(lead);

        Message message = new Message();
        message.setContent(content == null ? "" : content.trim());
        message.setDirection("INBOUND");
        message.setCreatedAt(now);
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
     */
    public List<Message> getLeadMessages(Lead lead) {

        if (lead == null) {
            return List.of();
        }

        return messageRepository.findByLeadOrderByCreatedAtAsc(lead);
    }

    /**
     * 🧠 Histórico completo por telefone
     */
    public List<Message> getLeadMessages(String phone) {

        Lead lead = findLeadByPhone(phone);

        if (lead == null) {
            return List.of();
        }

        return getLeadMessages(lead);
    }

    /**
     * 🧠 Últimas mensagens para contexto da IA
     * Evita enviar conversas gigantes para o LLM.
     */
    public List<Message> getRecentMessages(Lead lead) {

        if (lead == null) {
            return List.of();
        }

        return messageRepository
                .findTop20ByLeadOrderByCreatedAtDesc(lead);
    }

    /**
     * 🧠 Últimas mensagens por telefone
     */
    public List<Message> getRecentMessages(String phone) {

        Lead lead = findLeadByPhone(phone);

        if (lead == null) {
            return List.of();
        }

        return getRecentMessages(lead);
    }

    /**
     * 🕒 Quantidade total de interações
     */
    public long getInteractionCount(Lead lead) {

        if (lead == null) {
            return 0;
        }

        return getLeadMessages(lead).size();
    }

    /**
     * 🕒 Quantidade total de interações por telefone
     */
    public long getInteractionCount(String phone) {

        Lead lead = findLeadByPhone(phone);

        if (lead == null) {
            return 0;
        }

        return getInteractionCount(lead);
    }
}