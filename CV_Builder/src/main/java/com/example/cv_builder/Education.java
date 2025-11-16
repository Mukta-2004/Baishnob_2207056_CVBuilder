package com.example.cv_builder;

public class Education {
    private String exam;
    private String department;
    private String board;
    private String year;
    private String grade;

    public Education(String exam, String department, String board, String year, String grade) {
        this.exam = exam;
        this.department = department;
        this.board = board;
        this.year = year;
        this.grade = grade;
    }

    public String getExam() { return exam; }
    public String getDepartment() { return department; }
    public String getBoard() { return board; }
    public String getYear() { return year; }
    public String getGrade() { return grade; }
}
