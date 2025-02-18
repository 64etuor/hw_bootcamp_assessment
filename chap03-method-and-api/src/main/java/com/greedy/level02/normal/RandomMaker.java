package com.greedy.level02.normal;

public class RandomMaker {
    public static void main(String[] args) {
        System.out.println(randomUpperAlphabet(25));
    }

    public static int randomNumber(int min, int max) {

        return (int) (Math.random() * (max - min + 1)) + min;
    }

    public static String randomUpperAlphabet(int length) {
        StringBuilder alpha_list = new StringBuilder();
        for (int i = 0; i < length; i++) {
            char Alphabet = (char) (Math.random() * 26 + 65);
            alpha_list.append(Alphabet);
             }
        return alpha_list.toString();
    }

    public static String rockPaperScissors() {
        String result = "";
        int randNo = (int) (Math.random() * 3);

        if(randNo == 0) {
            return result = "가위";
        } else if(randNo == 1) {
            return result = "바위";
        } else {
            return result = "보";
        }
    }

    public static String tossCoin() {
        return (Math.random() * 2) > 1 ? "앞면" : "뒷면";
    }
}
