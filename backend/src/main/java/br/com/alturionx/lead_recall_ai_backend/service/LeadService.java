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

    public Lead findOrCreateLead(String phone) {

        if (phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Phone is required");
        }

        return leadRepository.findByPhone(phone)
                .orElseGet(() -> {
                    Lead lead = new Lead();
                    lead.setPhone(phone);
                    return leadRepository.save(lead);
                });
    }
}
