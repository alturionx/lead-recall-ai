package br.com.alturionx.lead_recall_ai_backend.integration.openai;

import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;

@Component
public class LeadPriorityEngine {

    public void apply(Lead lead, LeadInsight ai) {

        if (lead == null || ai == null) return;

        // =========================
        // INTENT (NÍVEL DE PRIORIDADE)
        // =========================
        if (isBetterIntent(ai.intent(), lead.getIntent())) {
            lead.setIntent(ai.intent());
        }

        // =========================
        // VEHICLE (NUNCA REGREDIR)
        // =========================
        if (isValid(ai.vehicle())) {

            if (isEmpty(lead.getVehicleInterest())) {
                lead.setVehicleInterest(ai.vehicle());
            } else if (isMoreSpecific(ai.vehicle(), lead.getVehicleInterest())) {
                lead.setVehicleInterest(ai.vehicle());
            }
        }

        // =========================
        // BUDGET (SÓ EVOLUI)
        // =========================
        if (ai.budget() != null && ai.budget() > 0) {

            if (lead.getBudget() == null) {
                lead.setBudget(ai.budget());
            } else if (ai.budget() > lead.getBudget()) {
                lead.setBudget(ai.budget());
            }
        }

        // =========================
        // CONFIDENCE (SÓ MELHORA)
        // =========================
        if (ai.confidence() != null && ai.confidence() > 0) {

            if (lead.getConfidence() == null) {
                lead.setConfidence(ai.confidence());
            } else if (ai.confidence() > lead.getConfidence()) {
                lead.setConfidence(ai.confidence());
            }
        }
    }

    // =========================
    // INTENT LEVEL SYSTEM
    // =========================

    private boolean isBetterIntent(String newIntent, String currentIntent) {

        int newLevel = intentLevel(newIntent);
        int currentLevel = intentLevel(currentIntent);

        return newLevel > currentLevel;
    }

    private int intentLevel(String intent) {

        if (intent == null) return 0;

        if (intent.equalsIgnoreCase("BUY_CAR")) return 2;

        return 0; // UNKNOWN
    }

    // =========================
    // VALIDATORS
    // =========================

    private boolean isValid(String value) {
        return value != null && !value.isBlank();
    }

    private boolean isEmpty(String value) {
        return value == null || value.isBlank();
    }

    // =========================
    // SPECIFICITY RULE
    // =========================

    private boolean isMoreSpecific(String newValue, String currentValue) {

        if (!isValid(newValue)) return false;

        if (isEmpty(currentValue)) return true;

        return newValue.length() > currentValue.length();
    }
}