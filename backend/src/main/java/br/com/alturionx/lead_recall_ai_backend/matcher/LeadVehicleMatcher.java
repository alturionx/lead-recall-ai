package br.com.alturionx.lead_recall_ai_backend.matcher;

import org.springframework.stereotype.Component;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;

import java.text.Normalizer;

@Component
public class LeadVehicleMatcher {

    public boolean matches(Lead lead, Vehicle vehicle) {

        if (lead == null || vehicle == null) return false;
        if (lead.getVehicleInterest() == null || vehicle.getModel() == null) return false;

        String interest = normalize(lead.getVehicleInterest());
        String model = normalize(vehicle.getModel());

        if (interest.isBlank() || model.isBlank()) return false;

        // 🔥 match mais inteligente
        boolean exactMatch = interest.equals(model);

        boolean partialMatch =
                interest.contains(model) ||
                model.contains(interest);

        boolean wordMatch =
                interest.split(" ").length > 1
                        ? interest.contains(model.split(" ")[0])
                        : false;

        boolean modelMatch = exactMatch || partialMatch || wordMatch;

        boolean budgetMatch =
                lead.getBudget() == null
                        || vehicle.getPrice() == null
                        || vehicle.getPrice().doubleValue() <= lead.getBudget();

        return modelMatch && budgetMatch;
    }

    private String normalize(String value) {
        return Normalizer.normalize(value, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "")
                .toLowerCase()
                .replaceAll("[^a-z0-9 ]", "")
                .replaceAll("\\s+", " ")
                .trim();
    }
}