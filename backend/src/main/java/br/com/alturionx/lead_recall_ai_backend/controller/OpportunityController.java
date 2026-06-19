package br.com.alturionx.lead_recall_ai_backend.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.alturionx.lead_recall_ai_backend.model.Opportunity;
import br.com.alturionx.lead_recall_ai_backend.repository.OpportunityRepository;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/opportunities")
public class OpportunityController {
    
    private final OpportunityRepository opportunityRepository;

    OpportunityController(OpportunityRepository opportunityRepository) {
        this.opportunityRepository = opportunityRepository;
    }
    
    @GetMapping
    public ResponseEntity<List<Opportunity>> findAll(){
        List<Opportunity> opportunities = opportunityRepository.findAll();
        return ResponseEntity.ok().body(opportunities);
    }
    
}
