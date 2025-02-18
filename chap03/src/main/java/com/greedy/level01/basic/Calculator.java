package com.greedy.level01.basic;

public class Calculator {
    public static void main(String[] args) {

    }
    public void checkMethod() {
        System.out.println("메소드 호출 확인");
    }
    public int sum1to10() {
        int sum = 0;
        for (int i =  1; i <= 10; i++) {
            sum += i;
            };
        return sum;
    }
    public int checkMaxNumber(int a, int b) {
        return Math.max(a, b);
    }

    public int sumTwoNumber(int a, int b) {
        return a + b;
        }
    public int minusTwoNumber(int a, int b) {
        return a - b;
    }
}
