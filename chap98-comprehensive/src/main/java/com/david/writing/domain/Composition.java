package com.david.writing.domain;

import java.io.Serializable;
import java.util.Date;

public class Composition implements Serializable {
    private final String id;
    private final Date created_at;
    private final String created_at_str;
    private final String originalSentence;
    private final ToneAndManner toneAndManner;
    private final String feedback;
    private final String aiSuggestedSentence;
    private final int compositionScore;
    private final String model;
    private final double temperature;

    public Composition(String id, Date created_at, String created_at_str, String originalSentence,
                      ToneAndManner toneAndManner, String feedback, String aiSuggestedSentence,
                      int compositionScore, String model, double temperature) {
        this.id = id;
        this.created_at = created_at;
        this.created_at_str = created_at_str;
        this.originalSentence = originalSentence;
        this.toneAndManner = toneAndManner;
        this.feedback = feedback;
        this.aiSuggestedSentence = aiSuggestedSentence;
        this.compositionScore = compositionScore;
        this.model = model;
        this.temperature = temperature;
    }

    public String getId() {
        return id;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public String getCreated_at_str() {
        return created_at_str;
    }

    public String getOriginalSentence() {
        return originalSentence;
    }

    public ToneAndManner getToneAndManner() {
        return toneAndManner;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getAiSuggestedSentence() {
        return aiSuggestedSentence;
    }

    public int getCompositionScore() {
        return compositionScore;
    }

    public String getModel() {
        return model;
    }

    public double getTemperature() {
        return temperature;
    }
} 