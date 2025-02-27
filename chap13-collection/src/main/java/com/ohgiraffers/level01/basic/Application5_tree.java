package com.ohgiraffers.level01.basic;

import java.util.Scanner;
import java.util.TreeSet;

/*
* 단어 입력 ('exit' 입력 시 종료): 안녕
단어 입력 ('exit' 입력 시 종료): java
단어 입력 ('exit' 입력 시 종료): collection
단어 입력 ('exit' 입력 시 종료): 프로그래밍
단어 입력 ('exit' 입력 시 종료): exit
정렬 된 단어 : [collection, java, 안녕, 프로그래밍]
* */
public class Application5_tree {
    public static void main(String[] args) {
        TreeSet<String> words = new TreeSet<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("단어 입력 ('exit' 입력 시 종료): ");
            String inputWord = sc.nextLine();
            
            if(inputWord.equalsIgnoreCase("exit")) {
                break;
            }
            words.add(inputWord);
        }
        System.out.println("정렬된 단어 : " + words);
    }
    
}
