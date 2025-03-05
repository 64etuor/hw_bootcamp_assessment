package com.david.writing.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONArray;
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
    private List<String> lastKoreanPrompts = new ArrayList<>();
    private List<Map<String, String>> lastVocabulary = new ArrayList<>();

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
                    Also provide 6 key vocabulary words with their English translations that are relevant to these sentences.
                    
                    Respond in JSON format only:
                    {
                        "sentences": [
                            "1. [First Korean sentence]",
                            "2. [Second Korean sentence]",
                            "3. [Third Korean sentence]"
                        ],
                        "vocabulary": [
                            {"word": "[Korean word 1]", "meaning": "[English meaning 1]"},
                            {"word": "[Korean word 2]", "meaning": "[English meaning 2]"},
                            {"word": "[Korean word 3]", "meaning": "[English meaning 3]"},
                            {"word": "[Korean word 4]", "meaning": "[English meaning 4]"},
                            {"word": "[Korean word 5]", "meaning": "[English meaning 5]"},
                            {"word": "[Korean word 6]", "meaning": "[English meaning 6]"}
                        ]
                    }
                    """;
                break;
            case NORMAL:
                prompt = """
                    Please create random three connected Korean sentences that could occur in daily life.
                    The sentences should flow naturally and use a casual, everyday tone in Korean.
                    Also provide 6 key vocabulary words with their English translations that are relevant to these sentences.
                    
                    Respond in JSON format only:
                    {
                        "sentences": [
                            "1. [First Korean sentence]",
                            "2. [Second Korean sentence]",
                            "3. [Third Korean sentence]"
                        ],
                        "vocabulary": [
                            {"word": "[Korean word 1]", "meaning": "[English meaning 1]"},
                            {"word": "[Korean word 2]", "meaning": "[English meaning 2]"},
                            {"word": "[Korean word 3]", "meaning": "[English meaning 3]"},
                            {"word": "[Korean word 4]", "meaning": "[English meaning 4]"},
                            {"word": "[Korean word 5]", "meaning": "[English meaning 5]"},
                            {"word": "[Korean word 6]", "meaning": "[English meaning 6]"}
                        ]
                    }
                    """;
                break;
            case ROMANTIC:
                prompt = """
                    Please create random three connected Korean sentences that could occur in a romantic situation.
                    The sentences should flow naturally and use an emotional and warm tone in Korean.
                    Also provide 6 key vocabulary words with their English translations that are relevant to these sentences.
                    
                    Respond in JSON format only:
                    {
                        "sentences": [
                            "1. [First Korean sentence]",
                            "2. [Second Korean sentence]",
                            "3. [Third Korean sentence]"
                        ],
                        "vocabulary": [
                            {"word": "[Korean word 1]", "meaning": "[English meaning 1]"},
                            {"word": "[Korean word 2]", "meaning": "[English meaning 2]"},
                            {"word": "[Korean word 3]", "meaning": "[English meaning 3]"},
                            {"word": "[Korean word 4]", "meaning": "[English meaning 4]"},
                            {"word": "[Korean word 5]", "meaning": "[English meaning 5]"},
                            {"word": "[Korean word 6]", "meaning": "[English meaning 6]"}
                        ]
                    }
                    """;
                break;
            case PLAYFUL:
                prompt = """
                    Please create random three connected Korean sentences about a fun and lighthearted situation.
                    The sentences should flow naturally and use a bright and humorous tone in Korean.
                    Also provide 6 key vocabulary words with their English translations that are relevant to these sentences.
                    
                    Respond in JSON format only:
                    {
                        "sentences": [
                            "1. [First Korean sentence]",
                            "2. [Second Korean sentence]",
                            "3. [Third Korean sentence]"
                        ],
                        "vocabulary": [
                            {"word": "[Korean word 1]", "meaning": "[English meaning 1]"},
                            {"word": "[Korean word 2]", "meaning": "[English meaning 2]"},
                            {"word": "[Korean word 3]", "meaning": "[English meaning 3]"},
                            {"word": "[Korean word 4]", "meaning": "[English meaning 4]"},
                            {"word": "[Korean word 5]", "meaning": "[English meaning 5]"},
                            {"word": "[Korean word 6]", "meaning": "[English meaning 6]"}
                        ]
                    }
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
        
        List<String> prompts = new ArrayList<>();
        JSONArray sentencesArray = jsonResponse.getJSONArray("sentences");
        for (int i = 0; i < sentencesArray.length(); i++) {
            prompts.add(sentencesArray.getString(i));
        }
        
        lastVocabulary.clear();
        JSONArray vocabularyArray = jsonResponse.getJSONArray("vocabulary");
        for (int i = 0; i < vocabularyArray.length(); i++) {
            JSONObject vocabItem = vocabularyArray.getJSONObject(i);
            Map<String, String> vocabMap = new HashMap<>();
            vocabMap.put("word", vocabItem.getString("word"));
            vocabMap.put("meaning", vocabItem.getString("meaning"));
            lastVocabulary.add(vocabMap);
        }
        
        this.lastKoreanPrompts = new ArrayList<>(prompts);
        
        return prompts;
    }

    private String getLastPrompts() {
        return String.join("\n", lastKoreanPrompts);
    }

    public Composition evaluateComposition(String originalText, ToneAndManner toneAndManner) throws IOException {
        String prompt = String.format("""
            You are an English writing evaluator. Please evaluate the following English translation.
            Evaluate with %s.
            
            Original Korean sentences:
            %s
            
            English translation to evaluate:
            %s
            
            Please respond in JSON format only with shorter feedback (keep it concise):
            {
                "improved_version": "Improved English sentence here",
                "score": score (0-100),
                "feedback": "Concise feedback focusing on grammar, vocabulary choice, and naturalness"
            }
            """, toneAndManner.getDescription(), getLastPrompts(), originalText);

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

        try {
           
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
            
            // 불완전한 JSON 복구 시도
            try {
                // 정상적인 JSON 파싱 시도
                int startIndex = response.indexOf('{');
                int endIndex = response.lastIndexOf('}');
                
                if (startIndex == -1 || endIndex == -1 || endIndex <= startIndex) {
                    // 중괄호가 없거나 손상된 경우, 최대한 복구 시도
                    startIndex = response.indexOf('{');
                    if (startIndex == -1) {
                        throw new IOException("JSON 시작 부분을 찾을 수 없습니다");
                    }
                    
                    // 불완전한 JSON 문자열 수정 시도
                    String partialJson = response.substring(startIndex);
                    
                    String improvedVersion = extractJsonField(partialJson, "improved_version");
                    String feedback = extractJsonField(partialJson, "feedback");
                    int score = extractJsonIntField(partialJson, "score", 0);
                    
                    if (improvedVersion == null || feedback == null) {
                        throw new IOException("필수 JSON 필드를 찾을 수 없습니다");
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
                
                // 정상 JSON 파싱
                response = response.substring(startIndex, endIndex + 1);

                JSONObject jsonResponse = new JSONObject(response);
                String improvedVersion = jsonResponse.optString("improved_version", "");
                String feedback = jsonResponse.optString("feedback", "");
                int score = jsonResponse.optInt("score", 0);
                
                if (improvedVersion.isEmpty() || feedback.isEmpty()) {
                    throw new IOException("Invalid AI response format: missing required fields");
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
            } catch (Exception jsonException) {
                System.err.println("JSON 파싱 오류: " + jsonException.getMessage());
                throw jsonException;
            }
        } catch (Exception e) {
            throw new IOException("Error processing API response: " + e.getMessage(), e);
        }
    }

    // JSON 문자열에서 특정 필드 값을 추출하는 헬퍼 메서드
    private String extractJsonField(String jsonString, String fieldName) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*\"([^\"]*(\\\\\"[^\"]*)*)*\"";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(jsonString);
        
        if (m.find()) {
            String match = m.group();
            int startIndex = match.indexOf(":") + 1;
            String value = match.substring(startIndex).trim();
            
            // 큰따옴표 제거
            if (value.startsWith("\"") && value.endsWith("\"")) {
                value = value.substring(1, value.length() - 1);
            }
            
            return value.replace("\\\"", "\"").replace("\\\\", "\\");
        }
        
        return null;
    }
    
    // JSON 문자열에서 숫자 필드 값을 추출하는 헬퍼 메서드
    private int extractJsonIntField(String jsonString, String fieldName, int defaultValue) {
        String pattern = "\"" + fieldName + "\"\\s*:\\s*(\\d+)";
        java.util.regex.Pattern r = java.util.regex.Pattern.compile(pattern);
        java.util.regex.Matcher m = r.matcher(jsonString);
        
        if (m.find()) {
            try {
                return Integer.parseInt(m.group(1));
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        
        return defaultValue;
    }

    public void setMaxTokens(int maxTokens) {
        apiConfig.setMaxTokens(maxTokens);
        apiConfigStorage.saveConfig(apiConfig);
    }

    public int getMaxTokens() {
        return apiConfig.getMaxTokens();
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

    public List<Map<String, String>> getLastVocabulary() {
        return new ArrayList<>(lastVocabulary);
    }
} 