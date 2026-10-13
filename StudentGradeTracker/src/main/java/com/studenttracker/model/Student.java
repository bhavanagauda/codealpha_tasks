
package com.studenttracker.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "students")
public class Student {

    @Id
    private String id;

    private String name;
    private String subject;
    private double score;

    public Student() {
    }

    public Student(String id, String name, String subject, double score) {
        this.id = id;
        this.name = name;
        this.subject = subject;
        this.score = score;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getGrade() {

        if (score >= 90) {
            return "A+";
        } else if (score >= 80) {
            return "A";
        } else if (score >= 70) {
            return "B";
        } else if (score >= 60) {
            return "C";
        } else if (score >= 50) {
            return "D";
        } else {
            return "F";
        }
    }

    public String getStatus() {

        if (score >= 40) {
            return "Pass";
        } else {
            return "Fail";
        }
    }
}