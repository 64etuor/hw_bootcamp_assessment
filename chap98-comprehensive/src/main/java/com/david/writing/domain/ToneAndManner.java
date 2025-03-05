package com.david.writing.domain;

public enum ToneAndManner {
    NORMAL("Normal English"),
    BUSINESS("Business English"),
    PLAYFUL("Playful English"),
    ROMANTIC("Romantic English");

    private final String description;

    ToneAndManner(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return description;
    }
} 