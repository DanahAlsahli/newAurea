package org.example.aurea.Service;

import org.example.aurea.Api.ApiException;
import org.example.aurea.Model.Recommendation;
import org.example.aurea.Repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationRepository recommendationRepository;

    public RecommendationService(
            RecommendationRepository recommendationRepository) {

        this.recommendationRepository =
                recommendationRepository;
    }

    public Recommendation addRecommendation(
            Recommendation recommendation) {

        return recommendationRepository.save(recommendation);
    }

    public List<Recommendation> getAllRecommendations() {

        return recommendationRepository.findAll();
    }

    public Recommendation getRecommendationById(
            Integer id) {

        return recommendationRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "Recommendation not found"));
    }

    public void updateRecommendation(
            Integer id,
            Recommendation newRecommendation) {

        Recommendation existing =
                recommendationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Recommendation not found"));

        existing.setAnalysisId(
                newRecommendation.getAnalysisId());

        existing.setTitle(
                newRecommendation.getTitle());

        existing.setDescription(
                newRecommendation.getDescription());

        existing.setPriority(
                newRecommendation.getPriority());

        existing.setStatus(
                newRecommendation.getStatus());

        existing.setImpact(
                newRecommendation.getImpact());

        existing.setEffort(
                newRecommendation.getEffort());

        existing.setProgress(
                newRecommendation.getProgress());

        recommendationRepository.save(existing);
    }

    public void deleteRecommendation(Integer id) {

        Recommendation existing =
                recommendationRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Recommendation not found"));

        recommendationRepository.delete(existing);
    }

    public List<Recommendation> getRecommendationsByAnalysis(
            Integer analysisId) {

        return recommendationRepository
                .findByAnalysisId(analysisId);
    }

    public List<Recommendation> getRecommendationsByPriority(
            Integer analysisId,
            String priority) {

        return recommendationRepository
                .findByAnalysisIdAndPriority(
                        analysisId,
                        priority);
    }

    public List<Recommendation> getRecommendationsByStatus(
            Integer analysisId,
            String status) {

        return recommendationRepository
                .findByAnalysisIdAndStatus(
                        analysisId,
                        status);
    }

    public List<Recommendation> getHighImpactRecommendations(Integer analysisId) {
        return recommendationRepository.findByAnalysisIdAndImpact(analysisId, "HIGH");
    }

    public List<Recommendation> getIncompleteRecommendations(Integer analysisId) {
        return recommendationRepository.findByAnalysisIdAndProgressLessThan(analysisId, 100);
    }
}


