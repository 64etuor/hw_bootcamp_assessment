package com.ohgiraffers.section01.conditional.level03.hard;

import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;

public class Application2 {

    public static void main(String[] args) {

        /* 과일 이름("사과", "바나나", "복숭아", "키위") 중 한 가지를 문자열로 입력하면
         * 해당하는 가격에 맞게 상품명과 가격이 출력되게 하세요.
         * 단, 목록에 없는 과일을 입력 시 "준비된 상품이 없습니다." 출력 후 프로그램 종료
         *
         * -- 상품 가격 --
         * 사과 :  1000원
         * 바나나 : 3000원
         * 복숭아 : 2000원
         * 키위 : 5000원
         *
         * -- 입력 예시 --
         * 과일 이름을 입력하세요 : 바나나
         *
         * -- 출력 예시 --
         * 바나나의 가격은 3000원 입니다.
         * */
        Map<String, Integer> fruitMap = new HashMap<>();
        fruitMap.put("사과", 1000);
        fruitMap.put("바나나", 3000);
        fruitMap.put("복숭아", 2000);
        fruitMap.put("키위", 5000);

        Scanner sc = new Scanner(System.in);
        System.out.println("과일 이름을 입력하세요 : ");
        String userInput = sc.nextLine();

        if (fruitMap.containsKey(userInput)) {
            System.out.println(userInput + "의 가격은 " + fruitMap.get(userInput) + "원 입니다.");
        } else {
            System.out.println("준비된 상품이 없습니다.");
        }

    }
}
        
        