package com.david.writing.config;

public class ApiConfig {
    private String apiKey;
    private String apiUrl;
    private double temperature;
    private int maxTokens;
    private String model;
    
    public static final String[] AVAILABLE_MODELS = {
        "deepseek-chat",
        "deepseek-coder",
        "deepseek-math"
    };

    public ApiConfig(String apiKey) {
        this.apiKey = apiKey;
        this.apiUrl = "https://api.deepseek.com/v1/chat/completions";
        this.temperature = 0.7;
        this.maxTokens = 600;
        this.model = "deepseek-chat";
    }

    public String getApiKey() {
        return apiKey;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        if (temperature < 0.0 || temperature > 1.5) {
            throw new IllegalArgumentException("Temperature must be between 0.0 and 1.5");
        }
        this.temperature = temperature;
    }

    public int getMaxTokens() {
        return maxTokens;
    }

    public void setMaxTokens(int maxTokens) {
        if (maxTokens < 100 || maxTokens > 2000) {
            throw new IllegalArgumentException("Max tokens must be between 100 and 2000");
        }
        this.maxTokens = maxTokens;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        for (String availableModel : AVAILABLE_MODELS) {
            if (availableModel.equals(model)) {
                this.model = model;
                return;
            }
        }
        throw new IllegalArgumentException("Unsupported model. Available models: " + String.join(", ", AVAILABLE_MODELS));
    }
} 