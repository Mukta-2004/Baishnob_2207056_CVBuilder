package com.example.cv_builder;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Education {

    private final StringProperty exam;
    private final StringProperty department;
    private final StringProperty board;
    private final StringProperty year;
    private final StringProperty grade;

    public Education(String exam, String department, String board, String year, String grade) {
        this.exam = new SimpleStringProperty(exam);
        this.department = new SimpleStringProperty(department);
        this.board = new SimpleStringProperty(board);
        this.year = new SimpleStringProperty(year);
        this.grade = new SimpleStringProperty(grade);
    }

    // Getters
    public String getExam() { return exam.get(); }
    public String getDepartment() { return department.get(); }
    public String getBoard() { return board.get(); }
    public String getYear() { return year.get(); }
    public String getGrade() { return grade.get(); }

    // Setters
    public void setExam(String value) { exam.set(value); }
    public void setDepartment(String value) { department.set(value); }
    public void setBoard(String value) { board.set(value); }
    public void setYear(String value) { year.set(value); }
    public void setGrade(String value) { grade.set(value); }


    public StringProperty examProperty() { return exam; }
    public StringProperty departmentProperty() { return department; }
    public StringProperty boardProperty() { return board; }
    public StringProperty yearProperty() { return year; }
    public StringProperty gradeProperty() { return grade; }
}
