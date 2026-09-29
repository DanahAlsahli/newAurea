package org.example.aurea.Controller;

import org.example.aurea.Model.Recommendation;
import org.example.aurea.Service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/recommendation")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(
            RecommendationService recommendationService) {

        this.recommendationService = recommendationService;
    }

    @PostMapping("/add")
    public ResponseEntity<?> addRecommendation(@RequestBody Recommendation recommendation) {

        return ResponseEntity
                .status(201)
                .body(
                        recommendationService
                                .addRecommendation(recommendation)
                );
    }

    // =========================
    // Read All
    // =========================

    @GetMapping("/get")
    public ResponseEntity<?> getAllRecommendations() {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getAllRecommendations()
                );
    }

    // =========================
    // Read By ID
    // =========================

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getRecommendationById(
            @PathVariable Integer id) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getRecommendationById(id)
                );
    }

    // =========================
    // Update
    // =========================

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateRecommendation(
            @PathVariable Integer id,
            @RequestBody Recommendation recommendation) {

        recommendationService.updateRecommendation(
                id,
                recommendation
        );

        return ResponseEntity
                .status(200)
                .body(
                        "Recommendation updated successfully"
                );
    }

    // =========================
    // Delete
    // =========================

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteRecommendation(
            @PathVariable Integer id) {

        recommendationService.deleteRecommendation(id);

        return ResponseEntity
                .status(200)
                .body(
                        "Recommendation deleted successfully"
                );
    }

    // =========================
    // Get By Analysis
    // =========================

    @GetMapping("/analysis/{analysisId}")
    public ResponseEntity<?> getRecommendationsByAnalysis(
            @PathVariable Integer analysisId) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getRecommendationsByAnalysis(
                                        analysisId
                                )
                );
    }

    // =========================
    // Get By Priority
    // =========================

    @GetMapping("/analysis/{analysisId}/priority/{priority}")
    public ResponseEntity<?> getRecommendationsByPriority(
            @PathVariable Integer analysisId,
            @PathVariable String priority) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getRecommendationsByPriority(
                                        analysisId,
                                        priority
                                )
                );
    }

    // =========================
    // Get By Status
    // =========================

    @GetMapping("/analysis/{analysisId}/status/{status}")
    public ResponseEntity<?> getRecommendationsByStatus(
            @PathVariable Integer analysisId,
            @PathVariable String status) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getRecommendationsByStatus(
                                        analysisId,
                                        status
                                )
                );
    }

    // =========================
    // High Impact
    // =========================

    @GetMapping("/analysis/{analysisId}/high-impact")
    public ResponseEntity<?> getHighImpactRecommendations(
            @PathVariable Integer analysisId) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getHighImpactRecommendations(
                                        analysisId
                                )
                );
    }

    // =========================
    // Incomplete
    // =========================

    @GetMapping("/analysis/{analysisId}/incomplete")
    public ResponseEntity<?> getIncompleteRecommendations(
            @PathVariable Integer analysisId) {

        return ResponseEntity
                .status(200)
                .body(
                        recommendationService
                                .getIncompleteRecommendations(
                                        analysisId
                                )
                );
    }
}




