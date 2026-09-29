package org.example.aurea.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatModel;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.example.aurea.Api.ApiException;
import org.example.aurea.Model.AIAnalysis;
import org.example.aurea.Model.Project;
import org.example.aurea.Repository.AIAnalysisRepository;
import org.example.aurea.Repository.ProjectRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AIAnalysisService {

    private final AIAnalysisRepository aiAnalysisRepository;
    private final ProjectRepository projectRepository;
    private final OpenAIClient openAIClient;
    private final ObjectMapper objectMapper;

    public AIAnalysisService(
            AIAnalysisRepository aiAnalysisRepository,
            ProjectRepository projectRepository) {

        this.aiAnalysisRepository = aiAnalysisRepository;
        this.projectRepository = projectRepository;
        this.openAIClient = OpenAIOkHttpClient.fromEnv();
        this.objectMapper = new ObjectMapper();
    }

    public AIAnalysis addAnalysis(AIAnalysis analysis) {

        if (analysis.getCreatedAt() == null) {
            analysis.setCreatedAt(LocalDateTime.now());
        }

        return aiAnalysisRepository.save(analysis);
    }

    public List<AIAnalysis> getAllAnalyses() {
        return aiAnalysisRepository.findAll();
    }

    public AIAnalysis getAnalysisById(Integer id) {

        return aiAnalysisRepository.findById(id)
                .orElseThrow(() ->
                        new ApiException(
                                "AI Analysis not found with id: " + id
                        )
                );
    }

    public void updateAnalysis(
            Integer id,
            AIAnalysis analysis) {

        AIAnalysis existingAnalysis =
                aiAnalysisRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "AI Analysis not found with id: " + id
                                )
                        );

        existingAnalysis.setProjectId(
                analysis.getProjectId()
        );

        existingAnalysis.setAnalysisType(
                analysis.getAnalysisType()
        );

        existingAnalysis.setScore(
                analysis.getScore()
        );

        existingAnalysis.setResult(
                analysis.getResult()
        );

        existingAnalysis.setStrengths(
                analysis.getStrengths()
        );

        existingAnalysis.setWeaknesses(
                analysis.getWeaknesses()
        );

        existingAnalysis.setAiRecommendation(
                analysis.getAiRecommendation()
        );

        existingAnalysis.setRiskLevel(
                analysis.getRiskLevel()
        );

        existingAnalysis.setConfidence(
                analysis.getConfidence()
        );

        existingAnalysis.setKeyDriver(
                analysis.getKeyDriver()
        );

        existingAnalysis.setExpectedOutcome(
                analysis.getExpectedOutcome()
        );

        aiAnalysisRepository.save(existingAnalysis);
    }

    public void deleteAnalysis(Integer id) {

        AIAnalysis existingAnalysis =
                aiAnalysisRepository.findById(id)
                        .orElseThrow(() ->
                                new ApiException(
                                        "AI Analysis not found with id: " + id
                                )
                        );

        aiAnalysisRepository.delete(existingAnalysis);
    }

    public AIAnalysis analyzeProject(Integer projectId) {

        Project project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Project not found with id: "
                                                + projectId
                                )
                        );

        String projectData = """
                Project Name: %s
                Description: %s
                Industry: %s
                Status: %s
                """.formatted(
                project.getName(),
                project.getDescription(),
                project.getIndustry(),
                project.getStatus()
        );

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O_MINI)

                        .addSystemMessage("""
                                You are an AI business project analyst.

                                Analyze the project information provided by the user.

                                Return ONLY valid JSON.

                                Use exactly this structure:

                                {
                                  "overallAnalysis": "A concise overall analysis of the project",
                                  "score": 85,
                                  "strengths": "Main strengths of the project",
                                  "weaknesses": "Main weaknesses of the project",
                                  "recommendations": "Practical recommendations for improving the project"
                                }

                                Rules:
                                - score must be a number from 0 to 100.
                                - Do not use Markdown.
                                - Do not use code fences.
                                - Do not add any text outside the JSON.
                                - All fields must be included.
                                """)

                        .addUserMessage(projectData)
                        .build();

        ChatCompletion response =
                openAIClient.chat()
                        .completions()
                        .create(params);

        String aiResult =
                response.choices()
                        .get(0)
                        .message()
                        .content()
                        .orElse("");

        AIAnalysis analysis = new AIAnalysis();

        analysis.setProjectId(projectId);
        analysis.setAnalysisType("AI_PROJECT_ANALYSIS");
        analysis.setCreatedAt(LocalDateTime.now());
        analysis.setResult(aiResult);

        try {

            JsonNode jsonNode =
                    objectMapper.readTree(aiResult);

            if (jsonNode.has("score")
                    && !jsonNode.get("score").isNull()) {

                analysis.setScore(
                        jsonNode.get("score").asInt()
                );
            }

            if (jsonNode.has("strengths")
                    && !jsonNode.get("strengths").isNull()) {

                analysis.setStrengths(
                        jsonNode.get("strengths").asText()
                );
            }

            if (jsonNode.has("weaknesses")
                    && !jsonNode.get("weaknesses").isNull()) {

                analysis.setWeaknesses(
                        jsonNode.get("weaknesses").asText()
                );
            }

            if (jsonNode.has("recommendations")
                    && !jsonNode.get("recommendations").isNull()) {

                analysis.setAiRecommendation(
                        jsonNode.get("recommendations").asText()
                );
            }

        } catch (Exception e) {

            analysis.setAiRecommendation(aiResult);
        }

        return aiAnalysisRepository.save(analysis);
    }

    public AIAnalysis simulateProject(
            Integer projectId,
            String scenario) {

        Project project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Project not found with id: "
                                                + projectId
                                )
                        );

        String projectData = """
                Project Name: %s
                Description: %s
                Industry: %s
                Status: %s
                Scenario: %s
                """.formatted(
                project.getName(),
                project.getDescription(),
                project.getIndustry(),
                project.getStatus(),
                scenario
        );

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O_MINI)

                        .addSystemMessage("""
                                You are an advanced AI business simulation analyst.

                                Simulate the effect of the given scenario
                                on the project.

                                Analyze:
                                - Expected impact
                                - Opportunities
                                - Risks
                                - Recommended actions

                                Return ONLY valid JSON.

                                Use exactly this structure:

                                {
                                  "overallAnalysis": "Overall simulation result",
                                  "score": 85,
                                  "strengths": "Positive effects and opportunities",
                                  "weaknesses": "Risks and negative effects",
                                  "recommendations": "Recommended actions"
                                }

                                Rules:
                                - score must be between 0 and 100.
                                - Do not use Markdown.
                                - Do not use code fences.
                                - Do not add any text outside the JSON.
                                - All fields must be included.
                                """)

                        .addUserMessage(projectData)
                        .build();

        ChatCompletion response =
                openAIClient.chat()
                        .completions()
                        .create(params);

        String aiResult =
                response.choices()
                        .get(0)
                        .message()
                        .content()
                        .orElse("");

        AIAnalysis analysis = new AIAnalysis();

        analysis.setProjectId(projectId);
        analysis.setAnalysisType("AI_PROJECT_SIMULATION");
        analysis.setResult(aiResult);
        analysis.setCreatedAt(LocalDateTime.now());

        try {

            JsonNode jsonNode =
                    objectMapper.readTree(aiResult);

            if (jsonNode.has("score")
                    && !jsonNode.get("score").isNull()) {

                analysis.setScore(
                        jsonNode.get("score").asInt()
                );
            }

            if (jsonNode.has("strengths")
                    && !jsonNode.get("strengths").isNull()) {

                analysis.setStrengths(
                        jsonNode.get("strengths").asText()
                );
            }

            if (jsonNode.has("weaknesses")
                    && !jsonNode.get("weaknesses").isNull()) {

                analysis.setWeaknesses(
                        jsonNode.get("weaknesses").asText()
                );
            }

            if (jsonNode.has("recommendations")
                    && !jsonNode.get("recommendations").isNull()) {

                analysis.setAiRecommendation(
                        jsonNode.get("recommendations").asText()
                );
            }

        } catch (Exception e) {

            analysis.setAiRecommendation(aiResult);
        }

        return aiAnalysisRepository.save(analysis);
    }

    public AIAnalysis decisionImpact(
            Integer projectId,
            String decision) {

        Project project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Project not found with id: "
                                                + projectId
                                )
                        );

        String projectData = """
                Project Name: %s
                Description: %s
                Industry: %s
                Status: %s
                Decision: %s
                """.formatted(
                project.getName(),
                project.getDescription(),
                project.getIndustry(),
                project.getStatus(),
                decision
        );

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O_MINI)

                        .addSystemMessage("""
                                You are Aurea, an advanced AI decision intelligence engine.

                                Evaluate the proposed business decision
                                and its potential impact on the project.

                                Analyze:
                                - Overall impact
                                - Expected outcome
                                - Opportunities
                                - Risks
                                - Risk level
                                - Confidence level
                                - Key decision driver
                                - Recommended actions
                                - Alternative decision

                                Return ONLY valid JSON.

                                Use exactly this structure:

                                {
                                  "overallAnalysis": "Overall decision impact",
                                  "impactScore": 85,
                                  "riskLevel": "MEDIUM",
                                  "confidence": 82,
                                  "keyDriver": "Main factor influencing the decision",
                                  "expectedOutcome": "Expected business outcome",
                                  "opportunities": "Potential opportunities",
                                  "risks": "Potential risks",
                                  "recommendations": "Recommended actions",
                                  "alternativeDecision": "Alternative decision to consider"
                                }

                                Rules:
                                - impactScore must be between 0 and 100.
                                - confidence must be between 0 and 100.
                                - riskLevel must be LOW, MEDIUM, or HIGH.
                                - Return valid JSON only.
                                - Do not use Markdown.
                                - Do not use code fences.
                                - Do not add text outside JSON.
                                """)

                        .addUserMessage(projectData)
                        .build();

        ChatCompletion response =
                openAIClient.chat()
                        .completions()
                        .create(params);

        String aiResult =
                response.choices()
                        .get(0)
                        .message()
                        .content()
                        .orElse("");

        AIAnalysis analysis = new AIAnalysis();

        analysis.setProjectId(projectId);
        analysis.setAnalysisType("AI_DECISION_IMPACT");
        analysis.setResult(aiResult);
        analysis.setCreatedAt(LocalDateTime.now());

        try {

            JsonNode jsonNode =
                    objectMapper.readTree(aiResult);

            if (jsonNode.has("impactScore")) {
                analysis.setScore(
                        jsonNode.get("impactScore").asInt()
                );
            }

            if (jsonNode.has("opportunities")) {
                analysis.setStrengths(
                        jsonNode.get("opportunities").asText()
                );
            }

            if (jsonNode.has("risks")) {
                analysis.setWeaknesses(
                        jsonNode.get("risks").asText()
                );
            }

            if (jsonNode.has("recommendations")) {
                analysis.setAiRecommendation(
                        jsonNode.get("recommendations").asText()
                );
            }

            if (jsonNode.has("riskLevel")) {
                analysis.setRiskLevel(
                        jsonNode.get("riskLevel").asText()
                );
            }

            if (jsonNode.has("confidence")) {
                analysis.setConfidence(
                        jsonNode.get("confidence").asInt()
                );
            }

            if (jsonNode.has("keyDriver")) {
                analysis.setKeyDriver(
                        jsonNode.get("keyDriver").asText()
                );
            }

            if (jsonNode.has("expectedOutcome")) {
                analysis.setExpectedOutcome(
                        jsonNode.get("expectedOutcome").asText()
                );
            }

        } catch (Exception e) {

            analysis.setAiRecommendation(aiResult);
        }

        return aiAnalysisRepository.save(analysis);
    }

    // =========================
    // AI ACTION PLAN
    // =========================

    public AIAnalysis actionPlan(Integer projectId) {

        Project project =
                projectRepository.findById(projectId)
                        .orElseThrow(() ->
                                new ApiException(
                                        "Project not found with id: "
                                                + projectId
                                )
                        );

        String projectData = """
                Project Name: %s
                Description: %s
                Industry: %s
                Status: %s
                """.formatted(
                project.getName(),
                project.getDescription(),
                project.getIndustry(),
                project.getStatus()
        );

        ChatCompletionCreateParams params =
                ChatCompletionCreateParams.builder()
                        .model(ChatModel.GPT_4O_MINI)

                        .addSystemMessage("""
                                You are Aurea, an advanced AI business strategy assistant.

                                Create a practical action plan for the project.

                                Analyze the project and identify the most important
                                actions that should be taken next.

                                Return ONLY valid JSON.

                                Use exactly this structure:

                                {
                                  "overallAnalysis": "Brief summary of the project situation",
                                  "priority": "HIGH",
                                  "actions": [
                                    {
                                      "title": "Action title",
                                      "description": "What should be done",
                                      "impact": "HIGH",
                                      "effort": "MEDIUM"
                                    },
                                    {
                                      "title": "Action title",
                                      "description": "What should be done",
                                      "impact": "MEDIUM",
                                      "effort": "LOW"
                                    },
                                    {
                                      "title": "Action title",
                                      "description": "What should be done",
                                      "impact": "MEDIUM",
                                      "effort": "MEDIUM"
                                    }
                                  ],
                                  "expectedOutcome": "Expected result after completing the actions"
                                }

                                Rules:
                                - Provide exactly 3 actions.
                                - Order actions from most important to least important.
                                - impact must be LOW, MEDIUM, or HIGH.
                                - effort must be LOW, MEDIUM, or HIGH.
                                - priority must be LOW, MEDIUM, or HIGH.
                                - Return valid JSON only.
                                - Do not use Markdown.
                                - Do not use code fences.
                                - Do not add text outside JSON.
                                """)

                        .addUserMessage(projectData)
                        .build();

        ChatCompletion response =
                openAIClient.chat()
                        .completions()
                        .create(params);

        String aiResult =
                response.choices()
                        .get(0)
                        .message()
                        .content()
                        .orElse("");

        AIAnalysis analysis = new AIAnalysis();

        analysis.setProjectId(projectId);
        analysis.setAnalysisType("AI_ACTION_PLAN");
        analysis.setResult(aiResult);
        analysis.setCreatedAt(LocalDateTime.now());

        try {

            JsonNode jsonNode =
                    objectMapper.readTree(aiResult);

            if (jsonNode.has("priority")) {

                analysis.setScore(
                        switch (jsonNode
                                .get("priority")
                                .asText()) {

                            case "HIGH" -> 90;
                            case "MEDIUM" -> 60;
                            default -> 30;
                        }
                );
            }

            if (jsonNode.has("actions")) {

                analysis.setStrengths(
                        jsonNode.get("actions").toString()
                );
            }

            if (jsonNode.has("expectedOutcome")) {

                analysis.setAiRecommendation(
                        jsonNode
                                .get("expectedOutcome")
                                .asText()
                );
            }

            if (jsonNode.has("overallAnalysis")) {

                analysis.setWeaknesses(
                        jsonNode
                                .get("overallAnalysis")
                                .asText()
                );
            }

        } catch (Exception e) {

            analysis.setAiRecommendation(aiResult);
        }

        return aiAnalysisRepository.save(analysis);
    }
}
