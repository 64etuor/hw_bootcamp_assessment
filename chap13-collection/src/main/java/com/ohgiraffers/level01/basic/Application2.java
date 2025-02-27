package com.ohgiraffers.level01.basic;

/*
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://www.google.com/
최근 방문 url : [https://www.google.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://github.com/
최근 방문 url : [https://github.com/, https://www.google.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://www.notion.so/
최근 방문 url : [https://www.notion.so/, https://github.com/, https://www.google.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://www.naver.com/
최근 방문 url : [https://www.naver.com/, https://www.notion.so/, https://github.com/, https://www.google.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://github.com/
최근 방문 url : [https://github.com/, https://www.naver.com/, https://www.notion.so/, https://github.com/, https://www.google.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : https://www.google.com/
최근 방문 url : [https://www.google.com/, https://github.com/, https://www.naver.com/, https://www.notion.so/, https://github.com/]
방문 URL을 입력하세요 (단, exit를 입력하면 종료) : exit
* */

import java.util.Scanner;
import java.util.Stack;

public class Application2 {
    public static void main(String[] args) {
        
        Stack<String> urlStack = new Stack<>();
        Scanner sc = new Scanner(System.in);
        
        while(true) {
            System.out.print("방문 URL을 입력하세요 (단, exit를 입력하면 종료) : ");
            String input = sc.nextLine();
            
            if(input.equalsIgnoreCase("exit")) {
                break;
            }
            urlStack.push(input);
            
            System.out.print("최근 방문 url : [");
            
            Stack<String> tempStack = new Stack<>();
            tempStack.addAll(urlStack);
            
            int size = Math.min(tempStack.size(), 5);
            for(int i = 0; i < size; i++) {
                System.out.print(tempStack.pop());
                if(i < size - 1) {
                    System.out.print(", ");
                }
            }
            System.out.println("]");
        }
        
        sc.close();
    }
}
