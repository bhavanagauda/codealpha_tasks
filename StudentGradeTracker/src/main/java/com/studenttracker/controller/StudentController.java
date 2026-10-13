
package com.studenttracker.controller;

import com.studenttracker.model.Student;
import com.studenttracker.repository.StudentRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@CrossOrigin
public class StudentController {

    private final StudentRepository repository;

    public StudentController(StudentRepository repository) {
        this.repository = repository;
    }

    // GET ALL STUDENTS
    @GetMapping
    public List<Student> getStudents() {
        return repository.findAll();
    }

    // GET STUDENT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getStudent(@PathVariable String id) {

        if (repository.existsById(id)) {
            return ResponseEntity.ok(repository.findById(id).get());
        }

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Student not found");
    }

    // ADD STUDENT
    @PostMapping
    public ResponseEntity<?> addStudent(@RequestBody Student student) {

        if (student.getId() == null || student.getId().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Student ID is required");
        }

        if (student.getName() == null || student.getName().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Student name is required");
        }

        if (student.getSubject() == null || student.getSubject().trim().isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("Subject is required");
        }

        if (student.getScore() < 0 || student.getScore() > 100) {
            return ResponseEntity
                    .badRequest()
                    .body("Score must be between 0 and 100");
        }

        if (repository.existsById(student.getId())) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body("Student ID already exists");
        }

        Student savedStudent = repository.save(student);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedStudent);
    }

    // UPDATE STUDENT
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(
            @PathVariable String id,
            @RequestBody Student student) {

        if (!repository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        if (student.getScore() < 0 || student.getScore() > 100) {
            return ResponseEntity
                    .badRequest()
                    .body("Score must be between 0 and 100");
        }

        Student existingStudent = repository.findById(id).get();

        existingStudent.setName(student.getName());
        existingStudent.setSubject(student.getSubject());
        existingStudent.setScore(student.getScore());

        Student updatedStudent = repository.save(existingStudent);

        return ResponseEntity.ok(updatedStudent);
    }

    // DELETE STUDENT
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteStudent(@PathVariable String id) {

        if (!repository.existsById(id)) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body("Student not found");
        }

        repository.deleteById(id);

        return ResponseEntity.ok("Student deleted successfully");
    }

    // SEARCH BY NAME
    @GetMapping("/search/name/{name}")
    public List<Student> searchByName(@PathVariable String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }

    // SEARCH BY SUBJECT
    @GetMapping("/search/subject/{subject}")
    public List<Student> searchBySubject(@PathVariable String subject) {
        return repository.findBySubjectContainingIgnoreCase(subject);
    }

    // STATISTICS
    @GetMapping("/statistics")
    public Statistics getStatistics() {

        List<Student> students = repository.findAll();

        if (students.isEmpty()) {
            return new Statistics(
                    0,
                    0,
                    0,
                    0,
                    0,
                    0
            );
        }

        double total = 0;
        double highest = students.get(0).getScore();
        double lowest = students.get(0).getScore();

        int passed = 0;
        int failed = 0;

        for (Student student : students) {

            double score = student.getScore();

            total += score;

            if (score > highest) {
                highest = score;
            }

            if (score < lowest) {
                lowest = score;
            }

            if (score >= 40) {
                passed++;
            } else {
                failed++;
            }
        }

        double average = total / students.size();

        return new Statistics(
                students.size(),
                average,
                highest,
                lowest,
                passed,
                failed
        );
    }

    // STATISTICS CLASS
    public static class Statistics {

        private int totalStudents;
        private double average;
        private double highest;
        private double lowest;
        private int passed;
        private int failed;

        public Statistics(
                int totalStudents,
                double average,
                double highest,
                double lowest,
                int passed,
                int failed) {

            this.totalStudents = totalStudents;
            this.average = average;
            this.highest = highest;
            this.lowest = lowest;
            this.passed = passed;
            this.failed = failed;
        }

        public int getTotalStudents() {
            return totalStudents;
        }

        public double getAverage() {
            return average;
        }

        public double getHighest() {
            return highest;
        }

        public double getLowest() {
            return lowest;
        }

        public int getPassed() {
            return passed;
        }

        public int getFailed() {
            return failed;
        }
    }
}
