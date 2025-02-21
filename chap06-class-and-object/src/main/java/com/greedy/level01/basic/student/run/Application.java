package com.greedy.level01.basic.student.run;

import java.util.Scanner;

import com.greedy.level01.basic.student.model.dto.StudentDTO;

public class Application{
    public static void main(String[] args) {
// 최대 10명의 학생 정보를 기록할 수 있게 배열을 할당한다.
// while문을 사용하여 학생들의 정보를 계속 입력 받고
// 한 명씩 추가 될 때마다 카운트함
// 계속 추가할 것인지 물어보고, 대소문자 상관없이 ‘y’이면 계속 객체 추가
// 3명 정도의 학생 정보를 입력 받아 각 객체에 저장함
// 현재 기록된 학생들의 각각의 점수 평균을 구함
// 학생들의 정보를 모두 출력 (평균 포함)
        Scanner scanner = new Scanner(System.in);

        StudentDTO[] students = new StudentDTO[10];
        int count = 0;
        boolean isContinue = true;
        
        while(isContinue && count < 10){
            StudentDTO student = new StudentDTO();
            
            try {
                System.out.println("학년 : ");
                student.setGrade(scanner.nextInt());

                System.out.println("반 : ");
                student.setClassroom(scanner.nextInt());

                System.out.println("이름 : ");
                student.setName(scanner.next());

                System.out.println("국어 : ");
                student.setKor(scanner.nextInt());

                System.out.println("영어 : ");
                student.setEng(scanner.nextInt());

                System.out.println("수학 : ");
                student.setMat(scanner.nextInt());
                
                students[count] = student;
                count++;
                
            } catch (IllegalArgumentException e) {
                System.out.println("오류: " + e.getMessage());
                System.out.println("다시 입력해주세요.");
                continue;
            }
            
            System.out.println("계속 추가 하시겠습니까? (y/n)");
            while(isContinue = true) {
                String answer = scanner.next();
                if(answer.equalsIgnoreCase("n")){
                    isContinue = false;
                    break;
                } else if(answer.equalsIgnoreCase("y")){
                    isContinue = true;
                    break;
                } else {
                    System.out.println("잘못 입력하셨습니다. y 또는 n을 입력해주세요.");
                    System.out.println("계속 추가 하시겠습니까? (y/n)");
                }
            }
        }
        for(int i = 0; i < count; i++){
            students[i].getInfomation();
        }
    }
} 