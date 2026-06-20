package br.com.alturionx.lead_recall_ai_backend.service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
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

        // INTENT
        if (insight.intent() != null
                && !insight.intent().isBlank()
                && !"UNKNOWN".equalsIgnoreCase(insight.intent())) {

            lead.setIntent(insight.intent().trim());
        }

        // VEHICLE
        if (insight.vehicle() != null
                && !insight.vehicle().isBlank()) {

            lead.setVehicleInterest(insight.vehicle().trim());
        }

        // BRAND (NOVO)
        if (insight.brand() != null
                && !insight.brand().isBlank()) {

            lead.setBrand(insight.brand().trim());
        }

        // MODEL (NOVO)
        if (insight.model() != null
                && !insight.model().isBlank()) {

            lead.setModel(insight.model().trim());
        }

        // VERSION (NOVO)
        if (insight.version() != null
                && !insight.version().isBlank()) {

            lead.setVersion(insight.version().trim());
        }

        // FUEL TYPE (NOVO)
        if (insight.fuelType() != null) {
            lead.setFuelType(insight.fuelType());
        }

        // TRANSMISSION (NOVO)
        if (insight.transmission() != null) {
            lead.setTransmission(insight.transmission());
        }

        // BUDGET
        if (insight.budget() != null
                && insight.budget() > 0) {

            lead.setBudget(insight.budget());
        }

        // CONFIDENCE
        if (insight.confidence() != null) {

            double confidence = insight.confidence();

            lead.setConfidence(confidence);
            lead.setCurrentConfidence(confidence);

            if (lead.getMaxConfidence() == null
                    || confidence > lead.getMaxConfidence()) {

                lead.setMaxConfidence(confidence);
            }
        }
    }

    /**
     * Score comercial permanente.
     */
    public void calculateScore(Lead lead) {

        if (lead == null) {
            return;
        }

        int score = 0;

        if ("BUY_CAR".equalsIgnoreCase(lead.getIntent())) {
            score += 40;
        }

        if (lead.getVehicleInterest() != null
                && !lead.getVehicleInterest().isBlank()) {
            score += 20;
        }

        if (lead.getBudget() != null) {

            if (lead.getBudget() >= 100000) {
                score += 20;
            } else if (lead.getBudget() >= 50000) {
                score += 15;
            } else {
                score += 10;
            }
        }

        if (lead.getMaxConfidence() != null) {
            score += (int) (lead.getMaxConfidence() * 20);
        }

        score = Math.min(score, 100);

        lead.setScore(score);

        calculateHeatScore(lead);
    }

    /**
     * Temperatura comercial atual.
     */
    private void calculateHeatScore(Lead lead) {

        int heatScore = lead.getScore() != null
                ? lead.getScore()
                : 0;

        if (lead.getLastInteractionAt() != null) {

            long days = ChronoUnit.DAYS.between(
                    lead.getLastInteractionAt(),
                    LocalDateTime.now());

            if (days <= 1) {
                heatScore += 10;
            } else if (days > 30) {
                heatScore -= 20;
            }
        }

        heatScore = Math.max(0, Math.min(heatScore, 100));

        lead.setHeatScore(heatScore);
    }
}