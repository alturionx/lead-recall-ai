package br.com.alturionx.lead_recall_ai_backend.automation;

import br.com.alturionx.lead_recall_ai_backend.enums.OpportunityStatus;
import br.com.alturionx.lead_recall_ai_backend.event.EventBus;
import br.com.alturionx.lead_recall_ai_backend.event.OpportunityCreatedEvent;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.model.Opportunity;
import br.com.alturionx.lead_recall_ai_backend.model.Vehicle;
import br.com.alturionx.lead_recall_ai_backend.repository.LeadRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.OpportunityRepository;
import br.com.alturionx.lead_recall_ai_backend.repository.VehicleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class OpportunityEventHandler {

    private final EventBus eventBus;
    private final OpportunityRepository opportunityRepository;
    private final LeadRepository leadRepository;
    private final VehicleRepository vehicleRepository;

    public OpportunityEventHandler(
            EventBus eventBus,
            OpportunityRepository opportunityRepository,
            LeadRepository leadRepository,
            VehicleRepository vehicleRepository
    ) {
        this.eventBus = eventBus;
        this.opportunityRepository = opportunityRepository;
        this.leadRepository = leadRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @PostConstruct
    public void init() {
        eventBus.subscribe(
                OpportunityCreatedEvent.class,
                this::handle
        );
    }

    private void handle(
            OpportunityCreatedEvent event
    ) {

        if (event == null) {
            return;
        }

        boolean exists =
                opportunityRepository
                        .existsByLeadIdAndVehicleId(
                                event.leadId(),
                                event.vehicleId()
                        );

        if (exists) {
            return;
        }

        Lead lead =
                leadRepository
                        .findById(event.leadId())
                        .orElse(null);

        Vehicle vehicle =
                vehicleRepository
                        .findById(event.vehicleId())
                        .orElse(null);

        if (lead == null || vehicle == null) {
            return;
        }

        Opportunity opportunity = new Opportunity();

        opportunity.setLead(lead);
        opportunity.setVehicle(vehicle);
        opportunity.setStatus(OpportunityStatus.NEW);

        int leadScore =
                lead.getScore() != null
                        ? lead.getScore()
                        : 0;

        int heatScore =
                lead.getHeatScore() != null
                        ? lead.getHeatScore()
                        : 0;

        double confidence =
                lead.getCurrentConfidence() != null
                        ? lead.getCurrentConfidence()
                        : 0.0;

        int confidenceScore =
                (int) (confidence * 20);

        int matchScore =
                event.matchScore() != null
                        ? event.matchScore()
                        : 0;

        /*
         * Score final:
         *
         * 50% Lead Score
         * 20% Heat Score
         * 10% Confidence
         * 20% Match Score
         */
        int finalScore =
                (int) (
                        (leadScore * 0.5)
                                + (heatScore * 0.2)
                                + (confidenceScore * 0.1)
                                + (matchScore * 0.2)
                );

        finalScore = Math.min(finalScore, 100);

        opportunity.setScore(finalScore);

        // 🔥 Dados reais vindos do matcher
        opportunity.setMatchScore(matchScore);

        opportunity.setMatchReason(
                event.matchReason()
        );

        opportunityRepository.save(opportunity);

        System.out.println(
                "🔥 OPPORTUNITY CREATED"
                        + " | lead=" + event.phone()
                        + " | match=" + matchScore
                        + " | score=" + finalScore
        );

        System.out.println(
                "📋 MATCH REASON: "
                        + event.matchReason()
        );
    }
}