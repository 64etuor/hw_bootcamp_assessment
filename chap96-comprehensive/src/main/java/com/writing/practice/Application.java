package com.writing.practice;

import com.writing.practice.config.DatabaseConfig;
import com.writing.practice.controller.MenuController;
import com.writing.practice.service.ai.AIService;
import com.writing.practice.service.ai.DeepseekChatStrategy;

public class Application {
    public static void main(String[] args) {
        // 데이터베이스 초기화
        DatabaseConfig.initializeDatabase();
        
        // AI 서비스 초기화
        AIService aiService = new DeepseekChatStrategy();
        
        // 메뉴 컨트롤러 생성 및 실행
        MenuController menuController = new MenuController(aiService);
        menuController.start();
    }
} 
