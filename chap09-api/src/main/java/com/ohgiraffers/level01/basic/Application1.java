package com.ohgiraffers.level01.basic;

import java.util.Scanner;

public class Application1 {

    public static void main(String[] args) {
        /* ----- 입력 예시 -----
         *
         * 문자열 입력 : I will be a good developer.
         *
         * ----- 출력 예시 -----
         *
         * 변환된 문자열: I Will Be A Good Developer.
         * 총 단어 개수: 6
         */

        Scanner sc = new Scanner(System.in);

        System.out.print("문자열 입력 : ");
        String str = sc.nextLine();
        
        StringBuilder modified = new StringBuilder(str);
        int convertedWordCount = 0;
        for (int i = 0; i < str.length(); i++) {
            if (modified.charAt(i) == ' ') {
                modified.setCharAt(i + 1, Character.toUpperCase(modified.charAt(i + 1)));
                convertedWordCount ++;
            }
        }
        
        System.out.println("변환된 문자열: " + modified.toString());
        System.out.println("총 단어 개수: " + convertedWordCount);

        sc.close();
    }
}