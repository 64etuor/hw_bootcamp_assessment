package com.david.writing.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.json.JSONObject;

import com.david.writing.client.DeepseekApiClient;
import com.david.writing.config.ApiConfig;
import com.david.writing.config.ApiConfigStorage;
import com.david.writing.domain.ApiUsage;
import com.david.writing.domain.Composition;
import com.david.writing.domain.ToneAndManner;
import com.david.writing.persistence.ApiUsageStorage;
import com.david.writing.persistence.CompositionRepository;
import com.david.writing.persistence.FileApiUsageStorage;

public class CompositionService {
    private final CompositionRepository repository;
    private final DeepseekApiClient apiClient;
    private final ApiConfig apiConfig;
    private final List<ApiUsage> apiUsageHistory;
    private final ApiUsageStorage apiUsageStorage;
    private final ApiConfigStorage apiConfigStorage;

    public CompositionService(CompositionRepository repository, String apiKey) {
        this.repository = repository;
        this.apiConfig = new ApiConfig(apiKey);
        this.apiUsageStorage = new FileApiUsageStorage();
        this.apiConfigStorage = new ApiConfigStorage();
        this.apiUsageHistory = new ArrayList<>(apiUsageStorage.loadApiUsage());
        
        this.apiConfigStorage.loadConfig(this.apiConfig);
        
        this.apiClient = new DeepseekApiClient(apiConfig);
    }

    public List<String> generateWritingPrompts(ToneAndManner selectedTone) throws IOException {
        String prompt;
        switch (selectedTone) {
            case BUSINESS:
                prompt = """
                    Please create random three connected Korean sentences that could occur in a business/workplace situation.
                    The sentences should flow naturally and use a professional and formal tone in Korean.
                    Please number each sentence with 1., 2., 3.
                    Respond in Korean only.
                    """;
                break;
            case NORMAL:
                prompt = """
                    Please create random three connected Korean sentences that could occur in daily life.
                    The sentences should flow naturally and use a casual, everyday tone in Korean.
                    Please number each sentence with 1., 2., 3.
                    Respond in Korean only.
                    """;
                break;
            case ROMANTIC:
                prompt = """
                    Please create random three connected Korean sentences that could occur in a romantic situation.
                    The sentences should flow naturally and use an emotional and warm tone in Korean.
                    Please number each sentence with 1., 2., 3.
                    Respond in Korean only.
                    """;
                break;
            case PLAYFUL:
                prompt = """
                    Please create random three connected Korean sentences about a fun and lighthearted situation.
                    The sentences should flow naturally and use a bright and humorous tone in Korean.
                    Please number each sentence with 1., 2., 3.
                    Respond in Korean only.
                    """;
                break;
            default:
                throw new IllegalArgumentException("Unsupported writing style.");
        }

        String response = apiClient.sendRequest(prompt);
        JSONObject usage = apiClient.getLastUsage();
        if (usage != null) {
            addApiUsage(new ApiUsage(
                new Date(),
                apiConfig.getModel(),
                usage.optInt("total_tokens", 0),
                usage.optInt("total_tokens", 0) * 0.000001
            ));
        }
        
        List<String> prompts = new ArrayList<>();
        String[] lines = response.split("\n");
        for (String line : lines) {
            if (line.trim().matches("\\d+\\..*")) {
                prompts.add(line.trim());
            }
        }
        return prompts;
    }

    public Composition evaluateComposition(String originalText, ToneAndManner toneAndManner) throws IOException {
        String prompt = String.format("""
            You are an English writing evaluator. Please evaluate the following English translation.
            Evaluate with %s.
            
            Translation to evaluate:
            %s
            
            Please respond in JSON format only:
            {
                "improved_version": "Improved English sentence here",
                "score": score (0-100),
                "feedback": "Detailed feedback focusing on grammar, vocabulary choice, and naturalness"
            }
            """, toneAndManner.getDescription(), originalText);

        String response = apiClient.sendRequest(prompt);
        JSONObject usage = apiClient.getLastUsage();
        if (usage != null) {
            addApiUsage(new ApiUsage(
                new Date(),
                apiConfig.getModel(),
                usage.optInt("total_tokens", 0),
                usage.optInt("total_tokens", 0) * 0.000001
            ));
        }

        // Clean up response and extract JSON
        response = response.trim();
        if (response.startsWith("```json")) {
            response = response.substring(7);
        }
        if (response.startsWith("```")) {
            response = response.substring(3);
        }
        if (response.endsWith("```")) {
            response = response.substring(0, response.length() - 3);
        }
        response = response.trim();

        JSONObject jsonResponse = new JSONObject(response);
        String improvedVersion = jsonResponse.optString("improved_version", "");
        String feedback = jsonResponse.optString("feedback", "");
        int score = jsonResponse.optInt("score", 0);

        if (improvedVersion.isEmpty() || feedback.isEmpty()) {
            throw new IOException("Invalid AI response format");
        }

        Composition composition = new Composition(
            UUID.randomUUID().toString(),
            new Date(),
            new Date().toString(),
            originalText,
            toneAndManner,
            feedback,
            improvedVersion,
            score,
            apiConfig.getModel(),
            apiConfig.getTemperature()
        );

        repository.save(composition);
        return composition;
    }

    public void setTemperature(double temperature) {
        apiConfig.setTemperature(temperature);
        apiConfigStorage.saveConfig(apiConfig);
    }

    public double getTemperature() {
        return apiConfig.getTemperature();
    }

    public String getModel() {
        return apiConfig.getModel();
    }

    public void setModel(String model) {
        apiConfig.setModel(model);
        apiConfigStorage.saveConfig(apiConfig);
    }

    public String[] getAvailableModels() {
        return ApiConfig.AVAILABLE_MODELS;
    }

    private void addApiUsage(ApiUsage usage) {
        apiUsageHistory.add(usage);
        apiUsageStorage.saveApiUsage(apiUsageHistory);
    }

    public List<ApiUsage> getApiUsageHistory() {
        return new ArrayList<>(apiUsageHistory);
    }

    public List<Composition> getCompositionHistory() {
        return repository.findAll();
    }
} 