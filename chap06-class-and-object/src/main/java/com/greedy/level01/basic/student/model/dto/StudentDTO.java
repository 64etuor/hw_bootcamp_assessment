package com.greedy.level01.basic.student.model.dto;

public class StudentDTO {
    private int grade;
    private int classroom;
    private String name;
    private int kor;
    private int eng;
    private int mat;

    public StudentDTO(){}
    
    public StudentDTO(int grade, int classroom, String name, int kor, int eng, int mat){
        this.grade = grade;
        this.classroom = classroom;
        this.name = name;
        this.kor = kor;
        this.eng = eng;
        this.mat = mat;
    }
    
    public void setGrade(int grade) { 
        if (grade >= 1 && grade <= 6) {
            this.grade = grade;
        } else {
            throw new IllegalArgumentException("학년은 1~6학년까지만 입력 가능합니다.");
        }
    }
    
    public void setClassroom(int classroom) { 
        if (classroom >= 1 && classroom <= 10) {
            this.classroom = classroom;
        } else {
            throw new IllegalArgumentException("반은 1~10반까지만 입력 가능합니다.");
        }
    }
    
    public void setName(String name) { this.name = name; }
    
    public void setKor(int kor) { 
        if (kor >= 0 && kor <= 100) {
            this.kor = kor;
        } else {
            throw new IllegalArgumentException("점수는 0~100점까지만 입력 가능합니다.");
        }
    }
    
    public void setEng(int eng) { 
        if (eng >= 0 && eng <= 100) {
            this.eng = eng;
        } else {
            throw new IllegalArgumentException("점수는 0~100점까지만 입력 가능합니다.");
        }
    }
    
    public void setMat(int mat) { 
        if (mat >= 0 && mat <= 100) {
            this.mat = mat;
        } else {
            throw new IllegalArgumentException("점수는 0~100점까지만 입력 가능합니다.");
        }
    }
    
    public int getGrade() { return grade; }
    public int getClassroom() { return classroom; }
    public String getName() { return name; }
    public int getKor() { return kor; }
    public int getEng() { return eng; }
    public int getMat() { return mat; }
    
    public String getInfomation() {
        String info = "학년 : " + getGrade() + ", "
                   + "반 : " + getClassroom() + ", "
                   + "이름 : " + getName() + ", "
                   + "국어 점수: " + getKor() + ", "
                   + "영어 점수: " + getEng() + ", "
                   + "수학 점수: " + getMat() + ", "
                   + "평균 점수: " + (getKor() + getEng() + getMat()) / 3;
        System.out.println(info);
        return info;
    }
}
