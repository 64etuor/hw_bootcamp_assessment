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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.david.writing.domain.ApiUsage;

public class FileApiUsageStorage implements ApiUsageStorage {
    private static final String STORAGE_FILE = "src/main/resources/db/api_usage.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public void saveApiUsage(List<ApiUsage> usageHistory) {
        File directory = new File("src/main/resources/db");
        if (!directory.exists()) {
            directory.mkdirs();
        }
        
        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(STORAGE_FILE), StandardCharsets.UTF_8))) {
            
            writer.write("Date,Model,Tokens,Cost");
            writer.newLine();

            for (ApiUsage usage : usageHistory) {
                writer.write(String.format("%s,%s,%d,%.6f",
                    DATE_FORMAT.format(usage.getTimestamp()),
                    usage.getModel(),
                    usage.getTokenCount(),
                    usage.getCost()
                ));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saving API usage data: " + e.getMessage());
        }
    }

    @Override
    public List<ApiUsage> loadApiUsage() {
        List<ApiUsage> usageHistory = new ArrayList<>();
        File file = new File(STORAGE_FILE);
        
        if (!file.exists()) {
            return usageHistory;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    usageHistory.add(parseCsvLine(line));
                } catch (Exception e) {
                    System.err.println("Error parsing API usage data: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading API usage data: " + e.getMessage());
        }

        return usageHistory;
    }

    private ApiUsage parseCsvLine(String line) throws ParseException {
        String[] values = line.split(",");
        if (values.length != 4) {
            throw new IllegalArgumentException("Invalid CSV format: " + line);
        }

        Date timestamp = DATE_FORMAT.parse(values[0]);
        String model = values[1];
        int tokenCount = Integer.parseInt(values[2]);
        double cost = Double.parseDouble(values[3]);

        return new ApiUsage(timestamp, model, tokenCount, cost);
    }
} 