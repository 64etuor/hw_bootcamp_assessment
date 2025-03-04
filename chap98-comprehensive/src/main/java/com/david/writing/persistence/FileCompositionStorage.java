package com.david.writing.persistence;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.david.writing.domain.Composition;
import com.david.writing.domain.ToneAndManner;

public class FileCompositionStorage implements CompositionStorage {
    private static final String STORAGE_FILE = "src/main/resources/db/compositions.csv";

    @Override
    public void saveCompositions(List<Composition> compositions) {
        File directory = new File("src/main/resources/db");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(STORAGE_FILE), StandardCharsets.UTF_8))) {
            
            writer.write("ID,Created At,Original Text,Writing Style,Feedback,Improved Text,Score,Model,Temperature");
            writer.newLine();

            for (Composition composition : compositions) {
                String csvLine = String.format("%s,%s,%s,%s,%s,%s,%d,%s,%.2f",
                    escapeField(composition.getId()),
                    escapeField(composition.getCreated_at_str()),
                    escapeField(composition.getOriginalSentence()),
                    escapeField(composition.getToneAndManner().name()),
                    escapeField(composition.getFeedback()),
                    escapeField(composition.getAiSuggestedSentence()),
                    composition.getCompositionScore(),
                    escapeField(composition.getModel()),
                    composition.getTemperature()
                );
                writer.write(csvLine);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving composition data: " + e.getMessage());
        }
    }

    @Override
    public List<Composition> loadCompositions() {
        List<Composition> compositions = new ArrayList<>();
        File file = new File(STORAGE_FILE);
        
        if (!file.exists()) {
            return compositions;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    compositions.add(parseCsvLine(line));
                } catch (Exception e) {
                    System.err.println("Error parsing CSV line: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading composition data: " + e.getMessage());
        }

        return compositions;
    }

    private Composition parseCsvLine(String line) {
        String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
        if (values.length != 9) {
            throw new IllegalArgumentException("Invalid CSV format: " + line);
        }

        String id = unescapeField(values[0]);
        String created_at_str = unescapeField(values[1]);
        String originalSentence = unescapeField(values[2]);
        ToneAndManner toneAndManner = ToneAndManner.valueOf(unescapeField(values[3]));
        String feedback = unescapeField(values[4]);
        String aiSuggestedSentence = unescapeField(values[5]);
        int score = Integer.parseInt(values[6]);
        String model = unescapeField(values[7]);
        double temperature = Double.parseDouble(values[8]);

        return new Composition(
            id,
            new Date(),
            created_at_str,
            originalSentence,
            toneAndManner,
            feedback,
            aiSuggestedSentence,
            score,
            model,
            temperature
        );
    }

    private String escapeField(String field) {
        if (field == null) return "";
        if (field.contains(",") || field.contains("\"") || field.contains("\n")) {
            return "\"" + field.replace("\"", "\"\"") + "\"";
        }
        return field;
    }

    private String unescapeField(String field) {
        if (field == null || field.isEmpty()) return "";
        if (field.startsWith("\"") && field.endsWith("\"")) {
            field = field.substring(1, field.length() - 1);
        }
        return field.replace("\"\"", "\"");
    }
} 