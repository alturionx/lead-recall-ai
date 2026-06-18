package br.com.alturionx.lead_recall_ai_backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.integration.openai.LeadInsight;

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

    // 🧠 aplica IA no lead
    public void applyInsight(Lead lead, LeadInsight insight) {

        lead.setIntent(insight.intent());
        lead.setVehicleInterest(insight.vehicle());
        lead.setBudget(insight.budget());
        lead.setConfidence(insight.confidence());
    }

    // 📊 SCORE CENTRALIZADO (corrigido)
    public void calculateScore(Lead lead) {

        int score = 0;

        if ("BUY_CAR".equals(lead.getIntent())) {
            score += 60;
        }

        if (lead.getVehicleInterest() != null) {
            score += 20;
        }

        if (lead.getBudget() != null && lead.getBudget() > 100000) {
            score += 20;
        }

        lead.setScore(score);
    }
}