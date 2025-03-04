package com.david.writing.config;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ApiConfigStorage {
    private static final String CONFIG_FILE = "src/main/resources/db/api_config.properties";

    public void saveConfig(ApiConfig config) {
        File directory = new File("src/main/resources/db");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        Properties props = new Properties();
        props.setProperty("temperature", String.valueOf(config.getTemperature()));
        props.setProperty("model", config.getModel());
        props.setProperty("maxTokens", String.valueOf(config.getMaxTokens()));

        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(CONFIG_FILE), StandardCharsets.UTF_8))) {
            props.store(writer, "API Configuration");
        } catch (IOException e) {
            System.err.println("Error saving configuration: " + e.getMessage());
        }
    }

    public void loadConfig(ApiConfig config) {
        Properties props = new Properties();
        File file = new File(CONFIG_FILE);
        
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), StandardCharsets.UTF_8))) {
            props.load(reader);

            String temperature = props.getProperty("temperature");
            String model = props.getProperty("model");
            String maxTokens = props.getProperty("maxTokens");

            if (temperature != null) {
                config.setTemperature(Double.parseDouble(temperature));
            }
            if (model != null) {
                config.setModel(model);
            }
        } catch (IOException e) {
            System.err.println("Error loading configuration: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.err.println("Invalid configuration value: " + e.getMessage());
        }
    }
} 