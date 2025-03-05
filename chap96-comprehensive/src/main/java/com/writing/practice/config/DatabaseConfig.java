package com.writing.practice.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConfig {
    private static final String DB_URL = "jdbc:sqlite:writing_practice.db";
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            
            // 작문 테이블 생성
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS compositions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    category TEXT NOT NULL,
                    korean_sentence TEXT NOT NULL,
                    user_sentence TEXT NOT NULL,
                    ideal_sentence TEXT NOT NULL,
                    feedback TEXT NOT NULL,
                    composition_score REAL NOT NULL,
                    model TEXT NOT NULL,
                    temperature REAL NOT NULL
                )
            """);

            // API 사용 정보 테이블 생성
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS api_usage (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                    endpoint TEXT NOT NULL,
                    tokens_used INTEGER NOT NULL,
                    model TEXT NOT NULL,
                    temperature REAL NOT NULL
                )
            """);

        } catch (SQLException e) {
            throw new RuntimeException("데이터베이스 초기화 중 오류 발생", e);
        }
    }
} 