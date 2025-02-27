package com.ohgiraffers.level01.basic;

/* 
 학생 성적 : 100
추가 입력하려면 y : y
학생 성적 : 95
추가 입력하려면 y : Y
학생 성적 : 66
추가 입력하려면 y : y
학생 성적 : 79
추가 입력하려면 y : n
학생 인원 : 4
평균 점수 : 85.0
 */

import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Application1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Collection<Integer> scores = new ArrayList<>();

        boolean answer = false;
        while(true) {
            System.out.println("학생 성적 : ");
            int score = sc.nextInt();
            scores.add(score);

            System.out.println("추가 입력하려면 y : ");
            String yn = sc.next();
            if(!yn.equalsIgnoreCase("y")) {
                break;
                }
        }

        int sum = 0;
        for(Integer score : scores) {
            sum += score;
        }
        double average = (double) sum / scores.size();
        System.out.println("학생 인원 : " + scores.size());
        System.out.println("평균 점수 : " + average);
    }
}
