package br.com.alturionx.lead_recall_ai_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;

@Service
public class LeadService {

    private final LeadRepository leadRepository;

    public LeadService(LeadRepository leadRepository) {
        this.leadRepository = leadRepository;
    }

    public List<Lead> findAll() {
        return leadRepository.findAll();
    }

    public Lead findByPhone(String phone) {
        return leadRepository.findByPhone(phone).orElse(null);
    }

    public Lead save(Lead lead) {
        return leadRepository.save(lead);
    }

    /**
     * Aplica informações extraídas pela IA sem destruir
     * contexto já conhecido do lead.
     */
    public void applyInsight(Lead lead, LeadInsight insight) {

        if (lead == null || insight == null) {
            return;
        }

        // Intent
        if (insight.intent() != null
                && !insight.intent().isBlank()
                && !"UNKNOWN".equalsIgnoreCase(insight.intent())) {

            lead.setIntent(insight.intent().trim());
        }

        // Veículo
        if (insight.vehicle() != null
                && !insight.vehicle().isBlank()) {

            lead.setVehicleInterest(insight.vehicle().trim());
        }

        // Orçamento
        if (insight.budget() != null
                && insight.budget() > 0) {

            lead.setBudget(insight.budget());
        }

        // Confiança
        if (insight.confidence() != null) {
            lead.setConfidence(insight.confidence());
        }
    }

    /**
     * Score baseado no estado atual do lead,
     * não na última mensagem recebida.
     */
    public void calculateScore(Lead lead) {

        int score = 0;

        if ("BUY_CAR".equalsIgnoreCase(lead.getIntent())) {
            score += 60;
        }

        if (lead.getVehicleInterest() != null
                && !lead.getVehicleInterest().isBlank()) {
            score += 20;
        }

        if (lead.getBudget() != null) {

            if (lead.getBudget() >= 100000) {
                score += 20;
            } else if (lead.getBudget() >= 50000) {
                score += 10;
            }
        }

        if (lead.getConfidence() != null) {
            score += (int) (lead.getConfidence() * 20);
        }

        lead.setScore(Math.min(score, 100));
    }
}