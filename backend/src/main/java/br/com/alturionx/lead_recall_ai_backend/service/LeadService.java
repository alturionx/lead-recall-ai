package br.com.alturionx.lead_recall_ai_backend.service;

import org.springframework.stereotype.Service;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    /**
     * 🔥 REGRA CORRETA:
     * - nunca cria lead "vazio" persistido
     * - só retorna entidade pronta pra enriquecimento
     */
    public Lead findOrCreateLead(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        return leadRepository.findByPhone(phone)
                .orElseGet(() -> createTransientLead(phone));
    }

    /**
     * ⚠️ IMPORTANTE:
     * Lead novo NÃO é salvo aqui.
     * Ele só será salvo depois da IA + Engine.
     */
    private Lead createTransientLead(String phone) {
        Lead lead = new Lead();
        lead.setPhone(phone);
        return lead;
    }
}