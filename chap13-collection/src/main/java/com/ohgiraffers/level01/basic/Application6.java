package com.ohgiraffers.level01.basic;

/*
* 이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): 홍길동010-1234-5678
입력이 잘못 되었습니다. 다음 양식으로 입력해주세요 : <이름> <전화번호>
이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): 홍길동 010-1234-5678
추가 완료 : 홍길동 010-1234-5678
이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): 유관순 010-9876-5432
추가 완료 : 유관순 010-9876-5432
이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): search
검색 할 이름 : 유관순
유관순씨의 전화번호 : 010-9876-5432
이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): search
검색 할 이름 : 이순신
이순신씨의 번호는 등록 되어 있지 않습니다.
이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit'): exit
* */

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Application6 {
    public static void main(String[] args) {
        Map<String, String> phoneBook = new HashMap<>();
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("이름과 전화번호를 띄어쓰기 기준으로 입력하세요 (검색하려면 'search', 종료하려면 'exit') :");
            String inputString = sc.nextLine();
            
            if(inputString.equalsIgnoreCase("exit")) {
                break;
            }
            
            if(inputString.equalsIgnoreCase("search")) {
                System.out.println("검색 할 이름 : ");
                String searchName = sc.nextLine();
                searchByName(phoneBook, searchName);
                continue;
            }
            
            String[] inputParts = inputString.split(" ");
            if(inputParts.length != 2) {
                System.out.println("입력이 잘못 되었습니다. 다음 양식으로 입력해주세요 : <이름> <전화번호>");
                continue;
            }
            
            String name = inputParts[0];
            String phoneNumber = inputParts[1];
            
            phoneBook.put(name, phoneNumber);
            System.out.println("추가 완료 : " + name + " " + phoneNumber);
        }

    }
    public static void searchByName(Map<String, String> phoneBook, String name) {
        String phoneNumber = phoneBook.get(name);
        if(phoneNumber == null) {
            System.out.println(name + "씨의 번호는 등록 되어 있지 않습니다.");
        } else {
            System.out.println(name + "씨의 전화번호 : " + phoneNumber);
        }
    }
}