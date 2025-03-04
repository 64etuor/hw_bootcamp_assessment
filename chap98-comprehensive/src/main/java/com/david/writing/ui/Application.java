package com.david.writing.ui;

import java.util.List;
import java.util.Scanner;

import com.david.writing.config.EnvConfig;
import com.david.writing.domain.ApiUsage;
import com.david.writing.domain.Composition;
import com.david.writing.domain.ToneAndManner;
import com.david.writing.persistence.CompositionRepository;
import com.david.writing.persistence.FileCompositionStorage;
import com.david.writing.service.CompositionService;

public class Application {
    private static final Scanner scanner = new Scanner(System.in);
    private static CompositionService compositionService;

    public static void main(String[] args) {
        try {
            initialize();
            showMenu();
        } catch (IllegalStateException e) {
            System.err.println("Program initialization failed: " + e.getMessage());
        }
    }

    private static void initialize() {
        CompositionRepository repository = new CompositionRepository(new FileCompositionStorage());
        compositionService = new CompositionService(repository, EnvConfig.getApiKey());
    }

    private static void showMenu() {
        while (true) {
            try {
                System.out.println("\n=== Writing Practice Program ===");
                System.out.println("1. Writing Practice");
                System.out.println("2. View Learning History");
                System.out.println("3. View API Usage");
                System.out.println("4. Set Temperature (Current: " + compositionService.getTemperature() + ")");
                System.out.println("5. Set AI Model (Current: " + compositionService.getModel() + ")");
                System.out.println("6. Exit");
                System.out.print("Select: ");

                String input = scanner.nextLine().trim();
                
                if (input.isEmpty()) {
                    System.out.println("Please enter your selection.");
                    continue;
                }

                try {
                    int choice = Integer.parseInt(input);
                    switch (choice) {
                        case 1:
                            startWritingPractice();
                            break;
                        case 2:
                            showLearningHistory();
                            break;
                        case 3:
                            showApiUsage();
                            break;
                        case 4:
                            setTemperature();
                            break;
                        case 5:
                            setModel();
                            break;
                        case 6:
                            System.out.println("Exiting program...");
                            return;
                        default:
                            System.out.println("Invalid selection. Please enter a number between 1-6.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Please enter a valid number.");
                }
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
            }
        }
    }

    private static void startWritingPractice() {
        while (true) {
            try {
                System.out.println("\n=== Select Writing Style ===");
                ToneAndManner[] styles = ToneAndManner.values();
                for (int i = 0; i < styles.length; i++) {
                    System.out.printf("%d. %s%n", i + 1, styles[i].getDescription());
                }
                System.out.println("0. Back to Main Menu");
                System.out.print("Select: ");

                String input = scanner.nextLine().trim();
                if (input.equals("0")) {
                    return;
                }

                int styleChoice = Integer.parseInt(input);
                if (styleChoice < 1 || styleChoice > styles.length) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                ToneAndManner selectedStyle = styles[styleChoice - 1];
                List<String> prompts = compositionService.generateWritingPrompts(selectedStyle);

                System.out.println("\n=== Korean Sentences ===");
                for (String prompt : prompts) {
                    System.out.println(prompt);
                }

                System.out.println("\nPlease translate these sentences into English.");
                System.out.println("Type your translation and press Enter after each line.");
                System.out.println("Type 'END' when finished or 'BACK' to return to style selection.");

                StringBuilder text = new StringBuilder();
                String line;
                while (!(line = scanner.nextLine()).equals("END")) {
                    if (line.equals("BACK")) {
                        continue;
                    }
                    text.append(line).append("\n");
                }

                if (text.length() == 0) {
                    System.out.println("No translation was entered.");
                    continue;
                }

                Composition result = compositionService.evaluateComposition(text.toString().trim(), selectedStyle);
                
                System.out.println("\n=== Evaluation Results ===");
                System.out.println("Score: " + result.getCompositionScore() + " points");
                System.out.println("\nImproved Version:");
                System.out.println(result.getAiSuggestedSentence());
                System.out.println("\nFeedback:");
                System.out.println(result.getFeedback());
                
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
                return;

            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                System.out.println("Would you like to try again? (Y/N)");
                if (!scanner.nextLine().trim().equalsIgnoreCase("Y")) {
                    return;
                }
            }
        }
    }

    private static void showLearningHistory() {
        List<Composition> history = compositionService.getCompositionHistory();
        if (history.isEmpty()) {
            System.out.println("\nNo learning history available.");
            return;
        }

        System.out.println("\n=== Learning History ===");
        for (Composition comp : history) {
            System.out.println("\nDate: " + comp.getCreated_at());
            System.out.println("Style: " + comp.getToneAndManner().getDescription());
            System.out.println("Original: " + comp.getOriginalSentence());
            System.out.println("Improved: " + comp.getAiSuggestedSentence());
            System.out.println("Score: " + comp.getCompositionScore());
            System.out.println("Feedback: " + comp.getFeedback());
            System.out.println("Model: " + comp.getModel() + ", Temperature: " + comp.getTemperature());
            System.out.println("-".repeat(50));
        }

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void showApiUsage() {
        List<ApiUsage> usageHistory = compositionService.getApiUsageHistory();
        if (usageHistory.isEmpty()) {
            System.out.println("\nNo API usage history available.");
            return;
        }

        System.out.println("\n=== API Usage History ===");
        int totalTokens = 0;
        double totalCost = 0;

        for (ApiUsage usage : usageHistory) {
            System.out.println(usage);
            totalTokens += usage.getTokenCount();
            totalCost += usage.getCost();
        }

        System.out.println("\nSummary:");
        System.out.printf("Total Tokens Used: %d%n", totalTokens);
        System.out.printf("Total Cost: $%.6f%n", totalCost);

        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    private static void setTemperature() {
        while (true) {
            try {
                System.out.println("\nEnter temperature (0.0-1.5):");
                double temperature = Double.parseDouble(scanner.nextLine().trim());
                compositionService.setTemperature(temperature);
                System.out.println("Temperature updated successfully.");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private static void setModel() {
        while (true) {
            try {
                System.out.println("\n=== Available Models ===");
                String[] models = compositionService.getAvailableModels();
                for (int i = 0; i < models.length; i++) {
                    System.out.printf("%d. %s%n", i + 1, models[i]);
                }
                System.out.print("Select model (1-" + models.length + "): ");

                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice < 1 || choice > models.length) {
                    System.out.println("Invalid selection. Please try again.");
                    continue;
                }

                compositionService.setModel(models[choice - 1]);
                System.out.println("Model updated successfully.");
                return;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
} 