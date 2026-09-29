package org.example.aurea.Controller;

import org.example.aurea.Model.AIAnalysis;
import org.example.aurea.Service.AIAnalysisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ai-analysis")
public class AIAnalysisController {
    private final AIAnalysisService aiAnalysisService;

    public AIAnalysisController(AIAnalysisService aiAnalysisService) {
        this.aiAnalysisService = aiAnalysisService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addAnalysis(@RequestBody AIAnalysis analysis) {
        return ResponseEntity.status(201).body(aiAnalysisService.addAnalysis(analysis));
    }

    @GetMapping("/get")
    public ResponseEntity<?> getAllAnalyses() {
        return ResponseEntity.status(200).body(aiAnalysisService.getAllAnalyses());
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getAnalysisById(@PathVariable Integer id) {
        return ResponseEntity.status(200).body(aiAnalysisService.getAnalysisById(id));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateAnalysis(@PathVariable Integer id, @RequestBody AIAnalysis analysis) {
        aiAnalysisService.updateAnalysis(id, analysis);

        return ResponseEntity.status(200).body("AI Analysis updated successfully");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteAnalysis(@PathVariable Integer id) {
        aiAnalysisService.deleteAnalysis(id);

        return ResponseEntity.status(200).body("AI Analysis deleted successfully");
    }

    @PostMapping("/analyze/{projectId}")
    public ResponseEntity<?> analyzeProject(@PathVariable Integer projectId) {
        return ResponseEntity.status(200).body(aiAnalysisService.analyzeProject(projectId));
    }

    @PostMapping("/simulate/{projectId}")
    public ResponseEntity<?> simulateProject(@PathVariable Integer projectId, @RequestBody String scenario) {
        return ResponseEntity.status(200).body(aiAnalysisService.simulateProject(projectId, scenario));
    }

    @PostMapping("/decision-impact/{projectId}")
    public ResponseEntity<?> decisionImpact(@PathVariable Integer projectId, @RequestBody String decision) {
        return ResponseEntity.status(200).body(aiAnalysisService.decisionImpact(projectId, decision));
    }

    @PostMapping("/action-plan/{projectId}")
    public ResponseEntity<?> actionPlan(@PathVariable Integer projectId) {
        return ResponseEntity.status(200).body(aiAnalysisService.actionPlan(projectId));
    }
}
