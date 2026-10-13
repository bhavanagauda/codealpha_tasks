package com.studenttracker.repository;

import com.studenttracker.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findBySubjectContainingIgnoreCase(String subject);
}