package com.david.writing.domain;

public enum ToneAndManner {
    NORMAL("일반적인 어조"),
    BUSINESS("비즈니스적인 어조"),
    PLAYFUL("장난스러운 어조"),
    ROMANTIC("로맨틱한 어조");

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