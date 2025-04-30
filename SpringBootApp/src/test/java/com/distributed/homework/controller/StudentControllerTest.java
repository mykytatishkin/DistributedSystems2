package com.distributed.homework.controller;

import com.distributed.homework.model.Student;
import com.distributed.homework.service.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentControllerTest {

    @Mock
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllStudents() {
        // Arrange
        Student student1 = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        Student student2 = new Student("Jane", "Smith", "jane@example.com", 
            "+0987654321", "456 Oak Ave", "Mathematics");
        List<Student> expectedStudents = Arrays.asList(student1, student2);
        when(studentService.getAllStudents()).thenReturn(expectedStudents);

        // Act
        ResponseEntity<List<Student>> response = studentController.getAllStudents();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudents, response.getBody());
    }

    @Test
    void getStudentById() {
        // Arrange
        Long id = 1L;
        Student expectedStudent = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentService.getStudentById(id)).thenReturn(Optional.of(expectedStudent));

        // Act
        ResponseEntity<Student> response = studentController.getStudentById(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedStudent, response.getBody());
    }

    @Test
    void getStudentById_NotFound() {
        // Arrange
        Long id = 1L;
        when(studentService.getStudentById(id)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Student> response = studentController.getStudentById(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void createStudent() {
        // Arrange
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentService.createStudent(any(Student.class))).thenReturn(student);

        // Act
        ResponseEntity<Student> response = studentController.createStudent(student);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(student, response.getBody());
    }

    @Test
    void updateStudent() {
        // Arrange
        Long id = 1L;
        Student updatedStudent = new Student("John", "Smith", "john.smith@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentService.updateStudent(eq(id), any(Student.class))).thenReturn(updatedStudent);

        // Act
        ResponseEntity<Student> response = studentController.updateStudent(id, updatedStudent);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedStudent, response.getBody());
    }

    @Test
    void deleteStudent() {
        // Arrange
        Long id = 1L;
        doNothing().when(studentService).deleteStudent(id);

        // Act
        ResponseEntity<Void> response = studentController.deleteStudent(id);

        // Assert
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
} 