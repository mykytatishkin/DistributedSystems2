package com.distributed.homework.service;

import com.distributed.homework.model.Student;
import com.distributed.homework.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the StudentServiceImpl class
 * <p>
 * These tests verify that the service correctly interacts with the repository
 * and handles various scenarios such as finding, creating, updating, and deleting students.
 * </p>
 * 
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

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
        when(studentRepository.findAll()).thenReturn(expectedStudents);

        // Act
        List<Student> actualStudents = studentService.getAllStudents();

        // Assert
        assertEquals(expectedStudents.size(), actualStudents.size());
        assertEquals(expectedStudents.get(0).getFirstName(), actualStudents.get(0).getFirstName());
        assertEquals(expectedStudents.get(1).getFirstName(), actualStudents.get(1).getFirstName());
    }

    @Test
    void getStudentById() {
        // Arrange
        Long id = 1L;
        Student expectedStudent = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentRepository.findById(id)).thenReturn(Optional.of(expectedStudent));

        // Act
        Optional<Student> actualStudent = studentService.getStudentById(id);

        // Assert
        assertTrue(actualStudent.isPresent());
        assertEquals(expectedStudent.getFirstName(), actualStudent.get().getFirstName());
    }

    @Test
    void createStudent() {
        // Arrange
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentRepository.save(any(Student.class))).thenReturn(student);

        // Act
        Student savedStudent = studentService.createStudent(student);

        // Assert
        assertNotNull(savedStudent);
        assertEquals(student.getFirstName(), savedStudent.getFirstName());
        verify(studentRepository, times(1)).save(student);
    }

    @Test
    void updateStudent() {
        // Arrange
        Long id = 1L;
        Student existingStudent = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        Student updatedStudent = new Student("John", "Smith", "john.smith@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentRepository.findById(id)).thenReturn(Optional.of(existingStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(updatedStudent);

        // Act
        Student result = studentService.updateStudent(id, updatedStudent);

        // Assert
        assertNotNull(result);
        assertEquals(updatedStudent.getLastName(), result.getLastName());
        assertEquals(updatedStudent.getEmail(), result.getEmail());
    }

    @Test
    void updateStudent_NotFound() {
        // Arrange
        Long id = 1L;
        Student updatedStudent = new Student("John", "Smith", "john.smith@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            studentService.updateStudent(id, updatedStudent);
        });
    }

    @Test
    void deleteStudent() {
        // Arrange
        Long id = 1L;
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        when(studentRepository.findById(id)).thenReturn(Optional.of(student));
        doNothing().when(studentRepository).delete(student);

        // Act
        studentService.deleteStudent(id);

        // Assert
        verify(studentRepository, times(1)).delete(student);
    }

    @Test
    void deleteStudent_NotFound() {
        // Arrange
        Long id = 1L;
        when(studentRepository.findById(id)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            studentService.deleteStudent(id);
        });
    }
} 