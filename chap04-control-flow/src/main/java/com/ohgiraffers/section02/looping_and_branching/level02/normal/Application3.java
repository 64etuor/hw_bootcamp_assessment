package com.ohgiraffers.section02.looping_and_branching.level02.normal;

import java.util.Scanner;

public class Application3 {

    public static void main(String[] args) {

        /* 정수를 입력받아 1부터 입력받은 정수까지
         * 홀수이면 "수", 짝수이면 "박"이 정수만큼 누적되어 출력되게 작성하시오.
         *
         * -- 입력 예시 --
         * 정수를 입력하세요 : 5
         *
         * -- 출력 예시 --
         * 수박수박수
         * */

        Scanner sc = new Scanner(System.in);
        System.out.println("정수를 입력하시오 : ");
        int num = sc.nextInt();

        for (int i = 1; i <= num; i++) {
            if (i % 2 != 0) {
                System.out.print("수");
            } else {
                System.out.print("박");
            }
        }

    }

}
