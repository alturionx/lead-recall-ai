package br.com.alturionx.lead_recall_ai_backend.controller;

import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.dto.CreateLeadRequest;
import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.service.LeadService;

@RestController
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    public Lead create(@RequestBody CreateLeadRequest request) {
        return leadService.findOrCreateLead(request.phone());
    }

    @GetMapping("/{phone}")
    public Lead getOrCreate(@PathVariable String phone) {
        return leadService.findOrCreateLead(phone);
    }
}