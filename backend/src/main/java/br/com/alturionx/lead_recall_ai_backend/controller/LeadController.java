package br.com.alturionx.lead_recall_ai_backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.alturionx.lead_recall_ai_backend.model.Lead;
import br.com.alturionx.lead_recall_ai_backend.service.LeadService;

@RestController
@RequestMapping("/leads")
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @GetMapping
    public ResponseEntity<List<Lead>> findAll() {
        return ResponseEntity.ok(leadService.findAll());
    }

    @GetMapping("/{phone}")
    public ResponseEntity<Lead> getByPhone(@PathVariable String phone) {

        Lead lead = leadService.findByPhone(phone);

        if (lead == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(lead);
    }
}