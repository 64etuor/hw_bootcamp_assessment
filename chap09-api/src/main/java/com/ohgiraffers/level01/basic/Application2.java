package com.ohgiraffers.level01.basic;

import java.util.Scanner;

public class Application2 {

    public static void main(String[] args) {

        /* ----- 입력 예시 -----
         *
         * 문자열 입력 : hello world Hello everyone! 안녕하세요 반갑습니다
         *
         * ----- 출력 예시 -----
         *
         * ===== 단어 빈도 =====
         * hello: 2
         * world: 1
         * everyone: 1
         * 가장 빈도 높은 단어 : hello (2 번)
         */

        Scanner sc = new Scanner(System.in);
        System.out.print("문자열 입력: ");

        String inputStr = sc.nextLine();
        String[] splitStr = inputStr.toLowerCase().split(" ");
                
        String[] words = new String[splitStr.length];
        int[] counts = new int[splitStr.length];
        int uniqueWordCount = 0;
        
        for(int i = 0; i < splitStr.length; i++) {
            boolean exists = false;
            
            for(int j = 0; j < uniqueWordCount; j++) {
                if(words[j].equals(splitStr[i])) {
                    counts[j]++;
                    exists = true;
                    break;
                }
            }
            
            if(!exists) {
                words[uniqueWordCount] = splitStr[i];
                counts[uniqueWordCount] = 1;
                uniqueWordCount++;
            }
        }
        
        int maxCount = 0;
        String mostFrequentWord = "";
        
        for(int i = 0; i < uniqueWordCount; i++) {
            if(counts[i] > maxCount) {
                maxCount = counts[i];
                mostFrequentWord = words[i];
            }
        }
        
        StringBuilder result = new StringBuilder();
        result.append("===== 단어 빈도 =====\n");
        
        for(int i = 0; i < uniqueWordCount; i++) {
            result.append(words[i]).append(": ").append(counts[i]).append("\n");
        }
        
        result.append("가장 빈도 높은 단어 : ").append(mostFrequentWord).append(" (").append(maxCount).append(" 번)");
        
        System.out.println(result.toString());
    }
}