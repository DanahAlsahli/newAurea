package org.example.aurea.Service;

import org.example.aurea.Api.ApiException;
import org.example.aurea.Model.AIAnalysis;
import org.example.aurea.Model.Project;
import org.example.aurea.Model.Recommendation;
import org.example.aurea.Repository.AIAnalysisRepository;
import org.example.aurea.Repository.ProjectRepository;
import org.example.aurea.Repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final AIAnalysisRepository aiAnalysisRepository;
    private final RecommendationRepository recommendationRepository;

    public ProjectService(
            ProjectRepository projectRepository,
            AIAnalysisRepository aiAnalysisRepository,
            RecommendationRepository recommendationRepository) {

        this.projectRepository = projectRepository;
        this.aiAnalysisRepository = aiAnalysisRepository;
        this.recommendationRepository = recommendationRepository;
    }

    public Project addProject(Project project) {
        return projectRepository.save(project);
    }

    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Project getProjectById(Integer id) {

        return projectRepository.findById(id).orElseThrow(() -> new ApiException("Project not found"));
    }

    public void updateProject(Integer id, Project project) {

        Project oldProject = projectRepository.findById(id).orElseThrow(() -> new ApiException("Project not found"));

        oldProject.setUserId(project.getUserId());
        oldProject.setName(project.getName());
        oldProject.setDescription(project.getDescription());
        oldProject.setIndustry(project.getIndustry());
        oldProject.setStatus(project.getStatus());

        projectRepository.save(oldProject);
    }

    public void deleteProject(Integer id) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ApiException("Project not found"));

        projectRepository.delete(project);
    }

    public Map<String, Object> getProjectDashboard(Integer id) {

        Project project = projectRepository.findById(id).orElseThrow(() -> new ApiException("Project not found"));
        List<AIAnalysis> analyses = aiAnalysisRepository.findByProjectId(id);
        Map<String, Object> dashboard = new HashMap<>();
        dashboard.put("project", project);
        dashboard.put("analyses", analyses);

        if (!analyses.isEmpty()) {

            AIAnalysis latestAnalysis = analyses.get(analyses.size() - 1);

            List<Recommendation> recommendations = recommendationRepository.findByAnalysisId(latestAnalysis.getId());

            dashboard.put(
                    "latestAnalysis",
                    latestAnalysis);

            dashboard.put(
                    "recommendations",
                    recommendations);

        } else {

            dashboard.put(
                    "latestAnalysis",
                    null);

            dashboard.put(
                    "recommendations",
                    List.of());
        }

        return dashboard;
    }

    public Map<String, Object> getProjectHealth(Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> health =
                new HashMap<>();

        health.put("projectId", id);
        health.put("projectName", project.getName());
        health.put("status", project.getStatus());

        if (analyses.isEmpty()) {

            health.put("healthScore", 0);
            health.put("healthLevel", "UNKNOWN");
            health.put(
                    "message",
                    "No AI analysis available yet.");

        } else {

            AIAnalysis latest =
                    analyses.get(analyses.size() - 1);

            int score =
                    latest.getScore() != null
                            ? latest.getScore()
                            : 0;

            health.put("healthScore", score);

            if (score >= 80) {

                health.put(
                        "healthLevel",
                        "HEALTHY");

            } else if (score >= 60) {

                health.put(
                        "healthLevel",
                        "STABLE");

            } else if (score >= 40) {

                health.put(
                        "healthLevel",
                        "AT_RISK");

            } else {

                health.put(
                        "healthLevel",
                        "CRITICAL");
            }

            health.put(
                    "latestAnalysis",
                    latest);
        }

        return health;
    }

    public Map<String, Object> getProjectInsights(Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> insights =
                new HashMap<>();

        insights.put("projectId", id);
        insights.put("projectName", project.getName());
        insights.put("totalAnalyses", analyses.size());
        insights.put("analyses", analyses);

        if (!analyses.isEmpty()) {

            AIAnalysis latest =
                    analyses.get(analyses.size() - 1);

            insights.put(
                    "latestInsight",
                    latest.getResult());

            insights.put(
                    "score",
                    latest.getScore());

            insights.put(
                    "strengths",
                    latest.getStrengths());

            insights.put(
                    "weaknesses",
                    latest.getWeaknesses());

            insights.put(
                    "recommendation",
                    latest.getAiRecommendation());
        }

        return insights;
    }

    public Map<String, Object> getProjectRisks(Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> risks =
                new HashMap<>();

        risks.put("projectId", id);
        risks.put("projectName", project.getName());

        List<Map<String, Object>> riskItems =
                analyses.stream()
                        .filter(a ->
                                a.getWeaknesses() != null)
                        .map(a -> {

                            Map<String, Object> risk =
                                    new HashMap<>();

                            risk.put(
                                    "analysisId",
                                    a.getId());

                            risk.put(
                                    "analysisType",
                                    a.getAnalysisType());

                            risk.put(
                                    "risk",
                                    a.getWeaknesses());

                            risk.put(
                                    "score",
                                    a.getScore());

                            return risk;

                        })
                        .toList();

        risks.put(
                "risks",
                riskItems);

        risks.put(
                "riskCount",
                riskItems.size());

        return risks;
    }

    public Map<String, Object> getProjectOpportunities(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> opportunities =
                new HashMap<>();

        opportunities.put(
                "projectId",
                id);

        opportunities.put(
                "projectName",
                project.getName());

        List<Map<String, Object>> opportunityItems =
                analyses.stream()
                        .filter(a ->
                                a.getStrengths() != null)
                        .map(a -> {

                            Map<String, Object> opportunity =
                                    new HashMap<>();

                            opportunity.put(
                                    "analysisId",
                                    a.getId());

                            opportunity.put(
                                    "analysisType",
                                    a.getAnalysisType());

                            opportunity.put(
                                    "opportunity",
                                    a.getStrengths());

                            return opportunity;

                        })
                        .toList();

        opportunities.put(
                "opportunities",
                opportunityItems);

        opportunities.put(
                "opportunityCount",
                opportunityItems.size());

        return opportunities;
    }

    public Map<String, Object> getProjectRecommendations(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "projectId",
                id);

        result.put(
                "projectName",
                project.getName());

        List<Map<String, Object>> recommendations =
                analyses.stream()
                        .filter(a ->
                                a.getAiRecommendation() != null)
                        .map(a -> {

                            Map<String, Object> recommendation =
                                    new HashMap<>();

                            recommendation.put(
                                    "analysisId",
                                    a.getId());

                            recommendation.put(
                                    "type",
                                    a.getAnalysisType());

                            recommendation.put(
                                    "recommendation",
                                    a.getAiRecommendation());

                            return recommendation;

                        })
                        .toList();

        result.put(
                "recommendations",
                recommendations);

        return result;
    }

    public Map<String, Object> getProjectPerformance(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> performance =
                new HashMap<>();

        performance.put(
                "projectId",
                id);

        performance.put(
                "projectName",
                project.getName());

        if (analyses.isEmpty()) {

            performance.put(
                    "averageScore",
                    0);

            performance.put(
                    "analysisCount",
                    0);

        } else {

            double average =
                    analyses.stream()
                            .filter(a ->
                                    a.getScore() != null)
                            .mapToInt(
                                    AIAnalysis::getScore)
                            .average()
                            .orElse(0);

            performance.put(
                    "averageScore",
                    Math.round(
                            average * 100.0)
                            / 100.0);

            performance.put(
                    "analysisCount",
                    analyses.size());

            performance.put(
                    "latestScore",
                    analyses.get(
                                    analyses.size() - 1)
                            .getScore());
        }

        return performance;
    }

    public Map<String, Object> getProjectForecast(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository.findByProjectId(id);

        Map<String, Object> forecast =
                new HashMap<>();

        forecast.put(
                "projectId",
                id);

        forecast.put(
                "projectName",
                project.getName());

        if (analyses.size() < 2) {

            forecast.put(
                    "forecastStatus",
                    "INSUFFICIENT_DATA");

            forecast.put(
                    "message",
                    "More AI analyses are required to generate a forecast.");

            return forecast;
        }

        AIAnalysis previous =
                analyses.get(
                        analyses.size() - 2);

        AIAnalysis latest =
                analyses.get(
                        analyses.size() - 1);

        Integer previousScore =
                previous.getScore();

        Integer latestScore =
                latest.getScore();

        if (previousScore == null ||
                latestScore == null) {

            forecast.put(
                    "forecastStatus",
                    "INSUFFICIENT_DATA");

            return forecast;
        }

        int change =
                latestScore - previousScore;

        forecast.put(
                "previousScore",
                previousScore);

        forecast.put(
                "latestScore",
                latestScore);

        forecast.put(
                "scoreChange",
                change);

        if (change > 0) {

            forecast.put(
                    "trend",
                    "IMPROVING");

        } else if (change < 0) {

            forecast.put(
                    "trend",
                    "DECLINING");

        } else {

            forecast.put(
                    "trend",
                    "STABLE");
        }

        return forecast;
    }

    public Map<String, Object> getEarlyWarnings(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository
                        .findByProjectIdOrderByCreatedAtDesc(id);

        Map<String, Object> result =
                new HashMap<>();

        result.put(
                "projectId",
                id);

        result.put(
                "projectName",
                project.getName());

        List<Map<String, Object>> warnings =
                new ArrayList<>();

        if (analyses.isEmpty()) {

            result.put(
                    "status",
                    "NO_DATA");

            result.put(
                    "warnings",
                    warnings);

            return result;
        }

        AIAnalysis latest =
                analyses.get(0);

        // High risk warning
        if ("HIGH".equalsIgnoreCase(
                latest.getRiskLevel())) {

            Map<String, Object> warning =
                    new HashMap<>();

            warning.put(
                    "type",
                    "HIGH_RISK");

            warning.put(
                    "severity",
                    "HIGH");

            warning.put(
                    "message",
                    "High risk detected in the latest project analysis.");

            warning.put(
                    "riskLevel",
                    latest.getRiskLevel());

            warning.put(
                    "analysisId",
                    latest.getId());

            warnings.add(warning);
        }

        // Score deterioration warning
        if (analyses.size() >= 2) {

            AIAnalysis previous =
                    analyses.get(1);

            if (latest.getScore() != null &&
                    previous.getScore() != null) {

                int change =
                        latest.getScore()
                                - previous.getScore();

                if (change <= -10) {

                    Map<String, Object> warning =
                            new HashMap<>();

                    warning.put(
                            "type",
                            "SCORE_DECLINE");

                    warning.put(
                            "severity",
                            change <= -20
                                    ? "HIGH"
                                    : "MEDIUM");

                    warning.put(
                            "message",
                            "Project health score has declined.");

                    warning.put(
                            "previousScore",
                            previous.getScore());

                    warning.put(
                            "currentScore",
                            latest.getScore());

                    warning.put(
                            "change",
                            change);

                    warnings.add(warning);
                }
            }
        }

        // Low confidence warning
        if (latest.getConfidence() != null &&
                latest.getConfidence() < 60) {

            Map<String, Object> warning =
                    new HashMap<>();

            warning.put(
                    "type",
                    "LOW_CONFIDENCE");

            warning.put(
                    "severity",
                    "MEDIUM");

            warning.put(
                    "message",
                    "AI confidence is relatively low.");

            warning.put(
                    "confidence",
                    latest.getConfidence());

            warnings.add(warning);
        }

        result.put(
                "status",
                warnings.isEmpty()
                        ? "HEALTHY"
                        : "WARNING");

        result.put(
                "warningCount",
                warnings.size());

        result.put(
                "warnings",
                warnings);

        return result;
    }

    public Map<String, Object> getExecutiveBrief(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository
                        .findByProjectIdOrderByCreatedAtDesc(id);

        Map<String, Object> brief =
                new HashMap<>();

        brief.put(
                "projectId",
                id);

        brief.put(
                "projectName",
                project.getName());

        brief.put(
                "industry",
                project.getIndustry());

        brief.put(
                "status",
                project.getStatus());

        if (analyses.isEmpty()) {

            brief.put(
                    "healthScore",
                    0);

            brief.put(
                    "riskLevel",
                    "UNKNOWN");

            brief.put(
                    "confidence",
                    0);

            brief.put(
                    "summary",
                    "No AI analysis is available yet.");

            return brief;
        }

        AIAnalysis latest =
                analyses.get(0);

        brief.put(
                "healthScore",
                latest.getScore());

        brief.put(
                "riskLevel",
                latest.getRiskLevel());

        brief.put(
                "confidence",
                latest.getConfidence());

        brief.put(
                "keyDriver",
                latest.getKeyDriver());

        brief.put(
                "expectedOutcome",
                latest.getExpectedOutcome());

        brief.put(
                "strengths",
                latest.getStrengths());

        brief.put(
                "risks",
                latest.getWeaknesses());

        brief.put(
                "recommendation",
                latest.getAiRecommendation());

        // Trend
        if (analyses.size() >= 2 &&
                latest.getScore() != null &&
                analyses.get(1).getScore() != null) {

            int change =
                    latest.getScore()
                            - analyses.get(1).getScore();

            brief.put(
                    "scoreChange",
                    change);

            if (change > 0) {

                brief.put(
                        "trend",
                        "IMPROVING");

            } else if (change < 0) {

                brief.put(
                        "trend",
                        "DECLINING");

            } else {

                brief.put(
                        "trend",
                        "STABLE");
            }

        } else {

            brief.put(
                    "scoreChange",
                    0);

            brief.put(
                    "trend",
                    "NO_BASELINE");
        }

        List<Recommendation> recommendations =
                recommendationRepository
                        .findByAnalysisIdOrderByIdDesc(
                                latest.getId());

        brief.put(
                "recommendations",
                recommendations);

        brief.put(
                "recommendationCount",
                recommendations.size());

        return brief;
    }

    public Map<String, Object> getDecisionMemory(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        List<AIAnalysis> analyses =
                aiAnalysisRepository
                        .findByProjectIdOrderByCreatedAtDesc(id);

        Map<String, Object> memory =
                new HashMap<>();

        memory.put(
                "projectId",
                id);

        memory.put(
                "projectName",
                project.getName());

        List<Map<String, Object>> decisions =
                new ArrayList<>();

        for (AIAnalysis analysis : analyses) {

            if (!"AI_DECISION_IMPACT"
                    .equals(analysis.getAnalysisType())) {

                continue;
            }

            Map<String, Object> decision =
                    new HashMap<>();

            decision.put(
                    "analysisId",
                    analysis.getId());

            decision.put(
                    "date",
                    analysis.getCreatedAt());

            decision.put(
                    "impactScore",
                    analysis.getScore());

            decision.put(
                    "riskLevel",
                    analysis.getRiskLevel());

            decision.put(
                    "confidence",
                    analysis.getConfidence());

            decision.put(
                    "keyDriver",
                    analysis.getKeyDriver());

            decision.put(
                    "expectedOutcome",
                    analysis.getExpectedOutcome());

            decision.put(
                    "recommendation",
                    analysis.getAiRecommendation());

            decisions.add(decision);
        }

        memory.put(
                "decisionCount",
                decisions.size());

        memory.put(
                "decisions",
                decisions);

        if (!decisions.isEmpty()) {

            memory.put(
                    "latestDecision",
                    decisions.get(0));

        } else {

            memory.put(
                    "latestDecision",
                    null);
        }

        return memory;
    }

    public Map<String, Object> getProjectIntelligence(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        AIAnalysis latest =
                aiAnalysisRepository
                        .findFirstByProjectIdOrderByCreatedAtDesc(id)
                        .orElse(null);

        Map<String, Object> intelligence =
                new HashMap<>();

        intelligence.put(
                "projectId",
                id);

        intelligence.put(
                "projectName",
                project.getName());

        if (latest == null) {

            intelligence.put(
                    "intelligenceScore",
                    0);

            intelligence.put(
                    "health",
                    "UNKNOWN");

            intelligence.put(
                    "riskLevel",
                    "UNKNOWN");

            intelligence.put(
                    "confidence",
                    0);

            return intelligence;
        }

        int score =
                latest.getScore() != null
                        ? latest.getScore()
                        : 0;

        int confidence =
                latest.getConfidence() != null
                        ? latest.getConfidence()
                        : 0;

        int riskPenalty = 0;

        if ("HIGH".equalsIgnoreCase(
                latest.getRiskLevel())) {

            riskPenalty = 20;

        } else if ("MEDIUM".equalsIgnoreCase(
                latest.getRiskLevel())) {

            riskPenalty = 10;
        }

        int intelligenceScore =
                (int) Math.round(
                        (score * 0.6)
                                + (confidence * 0.4)
                                - riskPenalty
                );

        intelligenceScore =
                Math.max(
                        0,
                        Math.min(
                                100,
                                intelligenceScore));

        String health;

        if (intelligenceScore >= 80) {

            health = "EXCELLENT";

        } else if (intelligenceScore >= 60) {

            health = "GOOD";

        } else if (intelligenceScore >= 40) {

            health = "AT_RISK";

        } else {

            health = "CRITICAL";
        }

        intelligence.put(
                "intelligenceScore",
                intelligenceScore);

        intelligence.put(
                "health",
                health);

        intelligence.put(
                "riskLevel",
                latest.getRiskLevel());

        intelligence.put(
                "confidence",
                confidence);

        intelligence.put(
                "impactScore",
                score);

        intelligence.put(
                "keyDriver",
                latest.getKeyDriver());

        intelligence.put(
                "expectedOutcome",
                latest.getExpectedOutcome());

        return intelligence;
    }

    public Map<String, Object> getExecutiveInsight(
            Integer id) {

        Project project =
                projectRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException("Project not found"));

        AIAnalysis latest =
                aiAnalysisRepository
                        .findFirstByProjectIdOrderByCreatedAtDesc(id)
                        .orElse(null);

        Map<String, Object> insight =
                new HashMap<>();

        insight.put(
                "projectId",
                id);

        insight.put(
                "projectName",
                project.getName());

        if (latest == null) {

            insight.put(
                    "insight",
                    "No AI analysis is available yet.");

            insight.put(
                    "priority",
                    "UNKNOWN");

            insight.put(
                    "risk",
                    "UNKNOWN");

            insight.put(
                    "nextAction",
                    "Run an AI project analysis.");

            return insight;
        }

        String risk =
                latest.getRiskLevel();

        Integer score =
                latest.getScore();

        String priority;

        if ("HIGH".equalsIgnoreCase(risk)
                || (score != null && score < 50)) {

            priority = "HIGH";

        } else if ("MEDIUM".equalsIgnoreCase(risk)
                || (score != null && score < 75)) {

            priority = "MEDIUM";

        } else {

            priority = "LOW";
        }

        String nextAction;

        if ("HIGH".equals(priority)) {

            nextAction =
                    "Review the major risks and execute the highest-priority recommendation.";

        } else if ("MEDIUM".equals(priority)) {

            nextAction =
                    "Monitor project performance and address the main decision drivers.";

        } else {

            nextAction =
                    "Maintain the current strategy and continue monitoring project performance.";
        }

        insight.put(
                "priority",
                priority);

        insight.put(
                "risk",
                risk);

        insight.put(
                "score",
                score);

        insight.put(
                "confidence",
                latest.getConfidence());

        insight.put(
                "keyDriver",
                latest.getKeyDriver());

        insight.put(
                "expectedOutcome",
                latest.getExpectedOutcome());

        insight.put(
                "insight",
                latest.getResult());

        insight.put(
                "recommendation",
                latest.getAiRecommendation());

        insight.put(
                "nextAction",
                nextAction);

        return insight;
    }
}


