package com.david.writing.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class EnvConfig {
    private static final Map<String, String> envVars = new HashMap<>();
    private static final String ENV_FILE = ".env";

    static {
        loadEnvFile();
    }

    private static void loadEnvFile() {
        Path envPath = Paths.get(ENV_FILE);
        if (Files.exists(envPath)) {
            try {
                Files.lines(envPath).forEach(line -> {
                    line = line.trim();
                    if (!line.isEmpty() && !line.startsWith("#")) {
                        String[] parts = line.split("=", 2);
                        if (parts.length == 2) {
                            envVars.put(parts[0].trim(), parts[1].trim());
                        }
                    }
                });
            } catch (IOException e) {
                System.err.println("Error reading environment file: " + e.getMessage());
            }
        }
    }

    public static String getApiKey() {
        String apiKey = System.getenv("DEEPSEEK_API_KEY");
        if (apiKey == null || apiKey.isEmpty()) {
            apiKey = envVars.get("DEEPSEEK_API_KEY");
        }
        if (apiKey == null || apiKey.isEmpty()) {
            throw new IllegalStateException("API key not set. Please set DEEPSEEK_API_KEY environment variable or create .env file.");
        }
        return apiKey;
    }
} 