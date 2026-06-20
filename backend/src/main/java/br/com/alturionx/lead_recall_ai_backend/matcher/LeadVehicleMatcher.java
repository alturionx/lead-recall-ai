package br.com.alturionx.lead_recall_ai_backend.matcher;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;
import org.springframework.stereotype.Component;

import java.text.Normalizer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Component
public class LeadVehicleMatcher {

    private static final int MIN_SCORE = 60;

    public MatchResult match(Lead lead, Vehicle vehicle) {

        if (lead == null || vehicle == null) {
            return new MatchResult(
                    0,
                    false,
                    false,
                    "Lead ou veículo inválido"
            );
        }

        if (lead.getVehicleInterest() == null
                || lead.getVehicleInterest().isBlank()) {

            return new MatchResult(
                    0,
                    false,
                    false,
                    "Lead sem interesse definido"
            );
        }

        if (vehicle.getModel() == null
                || vehicle.getModel().isBlank()) {

            return new MatchResult(
                    0,
                    false,
                    false,
                    "Veículo sem modelo"
            );
        }

        String interest = normalize(lead.getVehicleInterest());
        String model = normalize(vehicle.getModel());

        // ---------------------------
        // SCORE DE MODELO (principal)
        // ---------------------------
        int modelScore = calculateModelScore(interest, model);

        boolean modelMatch = modelScore >= MIN_SCORE;

        // ---------------------------
        // SCORE DE PREÇO (ajuste)
        // ---------------------------
        int budgetScore = calculateBudgetScore(lead, vehicle);

        // ---------------------------
        // SCORE FINAL
        // ---------------------------
        int finalScore = modelScore + budgetScore;
        finalScore = Math.min(finalScore, 100);

        // ORÇAMENTO NÃO BLOQUEIA MAIS
        boolean budgetMatch = budgetScore >= 0;

        /* boolean matched = finalScore >= MIN_SCORE; */

        String reason = buildReason(modelScore, budgetScore, finalScore);

        return new MatchResult(
                finalScore,
                modelMatch,
                budgetMatch,
                reason
        );
    }

    // ---------------------------
    // MODELO
    // ---------------------------
    private int calculateModelScore(String interest, String model) {

        if (interest.equals(model)) {
            return 100;
        }

        if (interest.contains(model) || model.contains(interest)) {
            return 85;
        }

        return calculateWordScore(interest, model);
    }

    // ---------------------------
    // PALAVRAS
    // ---------------------------
    private int calculateWordScore(String interest, String model) {

        Set<String> interestWords = new HashSet<>(Arrays.asList(interest.split(" ")));
        Set<String> modelWords = new HashSet<>(Arrays.asList(model.split(" ")));

        int matches = 0;

        for (String word : interestWords) {
            if (word.length() < 3) continue;
            if (modelWords.contains(word)) matches++;
        }

        return Math.min(matches * 25, 70);
    }

    // ---------------------------
    // ORÇAMENTO (NÃO BLOQUEIA MAIS)
    // ---------------------------
    private int calculateBudgetScore(Lead lead, Vehicle vehicle) {

        if (lead.getBudget() == null || vehicle.getPrice() == null) {
            return 0;
        }

        double budget = lead.getBudget();
        double price = vehicle.getPrice().doubleValue();

        double diff = budget - price;

        // dentro do orçamento
        if (diff >= 0) {
            return 20;
        }

        // até 10% acima
        if (Math.abs(diff) <= budget * 0.10) {
            return 5;
        }

        // até 20% acima
        if (Math.abs(diff) <= budget * 0.20) {
            return -5;
        }

        // muito fora
        return -20;
    }

    // ---------------------------
    // EXPLICAÇÃO
    // ---------------------------
    private String buildReason(int modelScore, int budgetScore, int finalScore) {

        if (modelScore >= 80) {
            return "Modelo altamente compatível";
        }

        if (modelScore >= 60) {
            return "Modelo compatível";
        }

        if (finalScore >= 60) {
            return "Compatibilidade parcial";
        }

        return "Baixa compatibilidade";
    }

    // ---------------------------
    // NORMALIZAÇÃO
    // ---------------------------
    private String normalize(String value) {

        if (value == null) return "";

        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}