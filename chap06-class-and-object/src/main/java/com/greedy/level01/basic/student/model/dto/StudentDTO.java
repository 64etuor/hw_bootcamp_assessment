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
    
    public void setGrade(int grade) { this.grade = grade; }
    public void setClassroom(int classroom) { this.classroom = classroom; }
    public void setName(String name) { this.name = name; }
    public void setKor(int kor) { this.kor = kor; }
    public void setEng(int eng) { this.eng = eng; }
    public void setMat(int mat) { this.mat = mat; }
    
    public int getGrade() { return grade; }
    public int getClassroom() { return classroom; }
    public String getName() { return name; }
    public int getKor() { return kor; }
    public int getEng() { return eng; }
    public int getMat() { return mat; }
    
    public String getInfomation() {
        String info = "학년 : " + grade + ", "
                   + "반 : " + classroom + ", "
                   + "이름 : " + name + ", "
                   + "국어 점수: " + kor + ", "
                   + "영어 점수: " + eng + ", "
                   + "수학 점수: " + mat + ", "
                   + "평균 점수: " + (kor + eng + mat) / 3;
        System.out.println(info);
        return info;
    }
}
