package com.distributed.homework.service;

import com.distributed.homework.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * Service interface for Student operations
 */
public interface StudentService {
    
    /**
     * Get all students
     * @return list of all students
     */
    List<Student> getAllStudents();
    
    /**
     * Get student by ID
     * @param id the student ID
     * @return the student if found
     */
    Optional<Student> getStudentById(Long id);
    
    /**
     * Create a new student
     * @param student the student to create
     * @return the created student
     */
    Student createStudent(Student student);
    
    /**
     * Update an existing student
     * @param id the student ID to update
     * @param student the updated student data
     * @return the updated student
     */
    Student updateStudent(Long id, Student student);
    
    /**
     * Delete a student
     * @param id the student ID to delete
     */
    void deleteStudent(Long id);
    
    /**
     * Find students by last name
     * @param lastName the last name to search for
     * @return list of matching students
     */
    List<Student> findStudentsByLastName(String lastName);
    
    /**
     * Find students by minimum age
     * @param age the minimum age
     * @return list of matching students
     */
    List<Student> findStudentsByMinimumAge(int age);
} 