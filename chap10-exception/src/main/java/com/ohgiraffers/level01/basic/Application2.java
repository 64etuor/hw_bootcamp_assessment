package com.ohgiraffers.level01.basic;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import com.ohgiraffers.level01.basic.exception.AgeRestrictionException;
import com.ohgiraffers.level01.basic.exception.DateFormatMismatchException;

public class Application2 {
    public static void main(String[] args) {

        /* ----- 입력 예시 -----
         *
         * 생년월일 입력 (yyyy-MM-dd 양식으로 작성) : 2014-05-05
         *
         * ----- 출력 예시 -----
         *
         * 만 20세 미만은 입장 불가입니다.
         *
         * ----- 입력 예시 -----
         *
         * 생년월일 입력 (yyyy-MM-dd 양식으로 작성) : 1994-05-05
         *
         * ----- 출력 예시 -----
         *
         * 입장하셔도 됩니다.
         *
         * ----- 입력 예시 -----
         *
         * 생년월일 입력 (yyyy-MM-dd 양식으로 작성) : 2000-14-15
         *
         * ----- 출력 예시 -----
         *
         * 날짜 양식을 잘못 입력하셨습니다.
         */

        Scanner sc = new Scanner(System.in);

        try {
            System.out.println("생년월일 입력 (yyyy-MM-dd 양식으로 작성) : ");
            String input = sc.nextLine();

            if (!input.matches("(19\\d{2}|20[0-2][0-5])-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01])")) {
                throw new DateFormatMismatchException("날짜 양식을 잘못 입력하셨습니다.");
            }

            LocalDate birthDate = LocalDate.parse(input, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate now = LocalDate.now();
            LocalDate ageLimit = now.minusYears(20);

            if (birthDate.isAfter(ageLimit)) {
                throw new AgeRestrictionException("만 20세 미만은 입장 불가입니다.");
            }

            System.out.println("입장하셔도 됩니다.");
            
        } catch (DateFormatMismatchException e) {
            System.out.println("오류 : " + e.getMessage());
            
        } catch (AgeRestrictionException e) {
            System.out.println("오류 : " + e.getMessage());
        }
    }
}