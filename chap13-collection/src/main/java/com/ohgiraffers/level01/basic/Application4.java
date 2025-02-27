package com.ohgiraffers.level01.basic;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

/*
* 학생 ID 입력('exit' 입력 시 종료): hello
ID가 추가 되었습니다.
학생 ID 입력('exit' 입력 시 종료): java
ID가 추가 되었습니다.
학생 ID 입력('exit' 입력 시 종료): programmer
ID가 추가 되었습니다.
학생 ID 입력('exit' 입력 시 종료): java
이미 등록 된 ID입니다.
학생 ID 입력('exit' 입력 시 종료): exit
모든 학생의 ID : [java, programmer, hello]
* */
public class Application4 {
    public static void main(String[] args) {
        Set<String> studentsID = new HashSet<>();
        Scanner sc = new Scanner(System.in);

        while (true) { 
            System.out.println("학생 ID 입력('exit' 입력시 종료: ");
            String inputStudentID = sc.nextLine();

            if(inputStudentID.equalsIgnoreCase("exit")) {
                break;
            }
            if(studentsID.contains(inputStudentID)) {
                System.out.println("이미 등록된 ID입니다.");

                
            } else { studentsID.add(inputStudentID);
                System.out.println("ID가 추가 되었습니다.");
            }
        }
        System.out.println("모든 학생의 ID : " + studentsID);
        
    }
}
