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

            lead.setVehicleInterest(
                    insight.vehicle().trim()
            );
        }

        // Orçamento
        if (insight.budget() != null
                && insight.budget() > 0) {

            lead.setBudget(insight.budget());
        }

        // Confiança
        if (insight.confidence() != null) {

            double confidence = insight.confidence();

            // Compatibilidade com código legado
            lead.setConfidence(confidence);

            // Última confiança observada
            lead.setCurrentConfidence(confidence);

            // Maior confiança histórica
            if (lead.getMaxConfidence() == null
                    || confidence > lead.getMaxConfidence()) {

                lead.setMaxConfidence(confidence);
            }
        }
    }

    /**
     * Score baseado no estado acumulado do lead.
     */
    public void calculateScore(Lead lead) {

        if (lead == null) {
            return;
        }

        int score = 0;

        // intenção de compra
        if ("BUY_CAR".equalsIgnoreCase(lead.getIntent())) {
            score += 40;
        }

        // veículo definido
        if (lead.getVehicleInterest() != null
                && !lead.getVehicleInterest().isBlank()) {
            score += 20;
        }

        // orçamento definido
        if (lead.getBudget() != null) {

            if (lead.getBudget() >= 100000) {
                score += 20;
            } else if (lead.getBudget() >= 50000) {
                score += 15;
            } else {
                score += 10;
            }
        }

        // usa a MAIOR confiança já observada
        if (lead.getMaxConfidence() != null) {
            score += (int) (lead.getMaxConfidence() * 20);
        }

        score = Math.min(score, 100);

        lead.setScore(score);

        // Heat Score inicialmente igual ao score.
        // Futuramente podemos adicionar recência,
        // engajamento, reativações e oportunidades.
        lead.setHeatScore(score);
    }
}