package com.writing.practice.controller;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.writing.practice.domain.apiusage.APIUsageRepository;
import com.writing.practice.domain.apiusage.APIUsageRepository.APIUsageStats;
import com.writing.practice.domain.composition.Composition;
import com.writing.practice.domain.composition.CompositionRepository;
import com.writing.practice.service.ai.AIService;
import com.writing.practice.service.ai.AIService.FeedbackResponse;
import com.writing.practice.service.ai.AIService.SentenceResponse;
import com.writing.practice.service.ai.AIService.WeaknessAnalysisResponse;

public class MenuController {
    private final AIService aiService;
    private final CompositionRepository compositionRepository;
    private final APIUsageRepository apiUsageRepository;
    private final Scanner scanner;

    public MenuController(AIService aiService) {
        // Windows 콘솔에서 한글 출력을 위한 설정
        try {
            new ProcessBuilder("cmd", "/c", "chcp", "65001").inheritIO().start().waitFor();
        } catch (Exception e) {
            System.err.println("콘솔 인코딩 설정 실패: " + e.getMessage());
        }

        this.aiService = aiService;
        this.compositionRepository = new CompositionRepository();
        this.apiUsageRepository = new APIUsageRepository();
        this.scanner = new Scanner(System.in, "UTF-8");
    }

    public void start() {
        while (true) {
            displayMenu();
            int choice = scanner.nextInt();
            scanner.nextLine(); // 버퍼 비우기

            switch (choice) {
                case 1 -> writingPractice();
                case 2 -> viewAPIUsage();
                case 3 -> weaknessAnalysis();
                case 4 -> setTemperature();
                case 5 -> setAIModel();
                case 6 -> setMaxToken();
                case 0 -> {
                    System.out.println("프로그램을 종료합니다.");
                    return;
                }
                default -> System.out.println("잘못된 선택입니다. 다시 선택해주세요.");
            }
        }
    }

    private void displayMenu() {
        System.out.println("""
            
            ===== Writing Practice Menu =====
            1. Writing Practice
            2. View API Usage
            3. Weakness Analysis
            4. Set Temperature (Current: %.1f)
            5. Set AI Model (Current: %s)
            6. Set Max Token (Current: %d)
            0. Exit
            선택하세요: """.formatted(
                aiService.getCurrentTemperature(),
                aiService.getCurrentModel(),
                aiService.getCurrentMaxTokens()
            ));
    }

    private void writingPractice() {
        System.out.println("""
            카테고리를 선택하세요:
            1. Normal
            2. Business
            3. Romantic
            4. Playful
            선택: """);
        
        int categoryChoice = scanner.nextInt();
        scanner.nextLine(); // 버퍼 비우기
        
        String category = switch (categoryChoice) {
            case 1 -> "Normal";
            case 2 -> "Business";
            case 3 -> "Romantic";
            case 4 -> "Playful";
            default -> {
                System.out.println("잘못된 선택입니다.");
                yield null;
            }
        };
        
        if (category == null) return;

        try {
            // 최근 작문 기록 출력
            System.out.println("\n=== 복습: 최근 작문 기록 ===");
            List<Composition> recentCompositions = compositionRepository.findRecentCompositions(30);
            
            // 복습 내용을 출력할 스레드 생성
            Thread reviewThread = new Thread(() -> {
                try {
                    for (Composition comp : recentCompositions.stream().limit(3).toList()) {
                        System.out.printf("""
                            
                            작성일: %s
                            한국어: %s
                            번역: %s
                            점수: %.1f
                            피드백: %s
                            
                            """,
                            comp.getCreatedAt(),
                            comp.getKoreanSentence(),
                            comp.getUserSentence(),
                            comp.getCompositionScore(),
                            comp.getFeedback()
                        );
                        Thread.sleep(5000); // 5초 대기
                    }
                } catch (InterruptedException e) {
                    // 스레드가 중단되면 조용히 종료
                }
            });
            reviewThread.start();

            // 랜덤 문장과 핵심 단어 가져오기
            System.out.println("새로운 문장을 가져오는 중입니다...");
            SentenceResponse response = aiService.getRandomSentences(category);
            
            // 복습 출력 중단
            reviewThread.interrupt();
            
            List<String> sentences = response.sentences();
            List<Map<String, String>> keywords = response.keywords();

            // 문장 제시 및 사용자 입력
            System.out.println("\n=== 오늘의 번역 과제 ===");
            for (int i = 0; i < sentences.size(); i++) {
                System.out.printf("%d. %s%n", i + 1, sentences.get(i));
            }

            System.out.println("\n각 문장의 번역을 입력해주세요.");
            List<String> translations = new ArrayList<>();
            
            for (int i = 0; i < sentences.size(); i++) {
                System.out.printf("\n%d번 문장 번역: ", i + 1);
                translations.add(scanner.nextLine());
            }

            // 피드백 요청 시작과 동시에 핵심 단어 출력 시작
            System.out.println("\n피드백을 요청하는 동안 오늘의 핵심 단어를 복습해보세요!");
            
            // 피드백 요청을 별도 스레드로 처리
            Thread feedbackThread = new Thread(() -> {
                try {
                    // 모든 문장에 대한 피드백을 한 번에 요청
                    List<FeedbackResponse> feedbacks = new ArrayList<>();
                    for (int i = 0; i < sentences.size(); i++) {
                        feedbacks.add(aiService.getFeedback(sentences.get(i), translations.get(i)));
                    }
                    
                    // 피드백 결과 출력 및 저장
                    System.out.println("\n=== 피드백 결과 ===");
                    for (int i = 0; i < sentences.size(); i++) {
                        FeedbackResponse feedback = feedbacks.get(i);
                        
                        System.out.printf("""
                            
                            === %d번 문장 ===
                            원문: %s
                            번역: %s
                            이상적인 번역: %s
                            점수: %.1f
                            피드백:
                            %s
                            
                            """,
                            i + 1,
                            sentences.get(i),
                            translations.get(i),
                            feedback.idealSentence(),
                            feedback.score(),
                            formatFeedback(feedback.feedback())
                        );
                        
                        // 결과 저장
                        Composition composition = new Composition(
                            category,
                            sentences.get(i),
                            translations.get(i),
                            feedback.idealSentence(),
                            feedback.feedback(),
                            feedback.score(),
                            aiService.getCurrentModel(),
                            aiService.getCurrentTemperature()
                        );
                        compositionRepository.save(composition);
                    }
                } catch (Exception e) {
                    System.out.println("\n피드백 처리 중 오류가 발생했습니다: " + e.getMessage());
                }
            });
            feedbackThread.start();

            // 핵심 단어 출력
            for (Map<String, String> keyword : keywords) {
                keyword.forEach((word, meaning) -> {
                    try {
                        Thread.sleep(1000);
                        System.out.printf("\n%s: %s", word, meaning);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                });
            }

            // 피드백 스레드가 완료될 때까지 대기
            feedbackThread.join();

            // 사용자가 Enter 키를 누를 때까지 대기
            System.out.println("\n\n메인 메뉴로 돌아가려면 Enter 키를 누르세요...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("오류가 발생했습니다: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void viewAPIUsage() {
        try {
            System.out.println("\n=== API 사용량 통계 ===");
            
            // 최근 30일 통계
            System.out.println("\n[최근 30일 통계]");
            printUsageStats(30);
            
            // 전체 통계
            System.out.println("\n[전체 통계]");
            printUsageStats(36500); // 약 100년치
            
            // 사용자가 Enter 키를 누를 때까지 대기
            System.out.println("\n메인 메뉴로 돌아가려면 Enter 키를 누르세요...");
            scanner.nextLine();
            
        } catch (Exception e) {
            System.out.println("통계 조회 중 오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void printUsageStats(int days) throws SQLException {
        APIUsageStats stats = apiUsageRepository.getUsageStats(days);
        
        System.out.printf("""
            총 API 호출 횟수: %d회
            총 토큰 사용량: %d tokens
            호출당 평균 토큰: %.1f tokens
            
            엔드포인트별 통계:
            """, 
            stats.totalCalls(),
            stats.totalTokens(),
            stats.avgTokensPerCall()
        );
        
        stats.endpointStats().forEach(endpoint -> 
            System.out.printf("- %s: %d회 호출, %d tokens 사용%n",
                endpoint.endpoint(),
                endpoint.calls(),
                endpoint.tokens()
            )
        );
    }

    private void weaknessAnalysis() {
        try {
            List<String> recentSentences = compositionRepository.findRecentUserSentences(30);
            if (recentSentences.isEmpty()) {
                System.out.println("분석할 작문 기록이 없습니다.");
                return;
            }

            System.out.println("\n약점을 분석하는 중입니다. 잠시만 기다려주세요...");
            
            // 분석 요청을 별도 스레드로 처리
            Thread analysisThread = new Thread(() -> {
                try {
                    WeaknessAnalysisResponse analysis = aiService.analyzeWeakness(recentSentences);
                    
                    System.out.println("\n=== 약점 분석 결과 ===");
                    System.out.println("\n발견된 약점:");
                    analysis.weaknesses().forEach(weakness -> System.out.println("- " + weakness));
                    
                    System.out.println("\n개선 제안:");
                    analysis.improvements().forEach(improvement -> System.out.println("- " + improvement));
                } catch (Exception e) {
                    System.out.println("\n분석 중 오류가 발생했습니다: " + e.getMessage());
                }
            });
            analysisThread.start();

            // 로딩 애니메이션 표시
            String[] frames = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
            int frameIndex = 0;
            while (analysisThread.isAlive()) {
                System.out.print("\r" + frames[frameIndex] + " 분석 중...");
                frameIndex = (frameIndex + 1) % frames.length;
                Thread.sleep(100);
            }
            System.out.println();

            // 분석 스레드가 완료될 때까지 대기
            analysisThread.join();

            // 사용자가 Enter 키를 누를 때까지 대기
            System.out.println("\n메인 메뉴로 돌아가려면 Enter 키를 누르세요...");
            scanner.nextLine();

        } catch (Exception e) {
            System.out.println("오류가 발생했습니다: " + e.getMessage());
        }
    }

    private void setTemperature() {
        System.out.print("Temperature 값을 입력하세요 (0~1.5): ");
        try {
            double temperature = scanner.nextDouble();
            aiService.setTemperature(temperature);
            System.out.println("Temperature가 설정되었습니다.");
        } catch (Exception e) {
            System.out.println("잘못된 입력입니다: " + e.getMessage());
        }
    }

    private void setAIModel() {
        System.out.println("현재 지원되는 모델: deepseek-chat");
        System.out.printf("사용할 모델을 입력하세요 (현재: %s): ", aiService.getCurrentModel());
        String model = scanner.nextLine().trim();
        
        if (model.isEmpty() || !model.equals("deepseek-chat")) {
            System.out.println("유효하지 않은 모델입니다. 현재 모델을 유지합니다.");
            return;
        }
        
        aiService.setModel(model);
        System.out.println("AI 모델이 설정되었습니다.");
    }

    private void setMaxToken() {
        System.out.print("최대 토큰 수를 입력하세요 (200~2000): ");
        try {
            int maxTokens = scanner.nextInt();
            aiService.setMaxTokens(maxTokens);
            System.out.println("최대 토큰 수가 설정되었습니다.");
        } catch (Exception e) {
            System.out.println("잘못된 입력입니다: " + e.getMessage());
        }
    }

    // 피드백 텍스트 포맷팅을 위한 헬퍼 메소드
    private String formatFeedback(String feedback) {
        // 약 50자마다 줄바꿈 추가
        StringBuilder formatted = new StringBuilder();
        String[] words = feedback.split(" ");
        int lineLength = 0;
        
        for (String word : words) {
            if (lineLength + word.length() > 50) {
                formatted.append("\n");
                lineLength = 0;
            }
            formatted.append(word).append(" ");
            lineLength += word.length() + 1;
        }
        
        return formatted.toString().trim().replaceAll("\n", "\n    "); // 들여쓰기 추가
    }
} 