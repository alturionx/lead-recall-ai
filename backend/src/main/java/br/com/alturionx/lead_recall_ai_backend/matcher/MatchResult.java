package br.com.alturionx.lead_recall_ai_backend.matcher;

public record MatchResult(

        // score total de compatibilidade (0–100)
        int score,

        // compatibilidade de modelo (ex: Duster vs Duster)
        boolean modelMatch,

        // se está dentro do budget
        boolean budgetMatch,

        // explicação humana (IMPORTANTE pro CRM)
        String reason

) {
    
    public boolean isOpportunity() {
        return score >= 70;
    }
}