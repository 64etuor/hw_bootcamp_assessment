package com.ohgiraffers.level01.basic;

/*
* 단어 입력 ('exit' 입력 시 종료): 안녕
단어 입력 ('exit' 입력 시 종료): java
단어 입력 ('exit' 입력 시 종료): collection
단어 입력 ('exit' 입력 시 종료): 프로그래밍
단어 입력 ('exit' 입력 시 종료): exit
정렬 된 단어 : [collection, java, 안녕, 프로그래밍]
* */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Application5 {
    public static void main(String[] args) {
        List<String> words = new ArrayList<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("단어 입력 ('exit' 입력 시 종료): ");
            String inputWord = sc.nextLine();
            
            if(inputWord.equalsIgnoreCase("exit")) {
                break;
            }
            words.add(inputWord);
        }
        words.sort(Comparator.naturalOrder());
        System.out.println("정렬된 단어 : " + words);

    }
}
