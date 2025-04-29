package com.distributed.homework.service;

import com.distributed.homework.model.Student;
import com.distributed.homework.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;
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
@ExtendWith(MockitoExtension.class)
public class StudentServiceImplTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentServiceImpl studentService;

    private Student testStudent;
    private List<Student> testStudents;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        testStudent = new Student("John", "Doe", "john.doe@example.com", 20);
        testStudent.setId(1L);
        
        Student student2 = new Student("Jane", "Smith", "jane.smith@example.com", 22);
        student2.setId(2L);
        
        testStudents = Arrays.asList(testStudent, student2);
    }

    /**
     * Test that getAllStudents returns all students from the repository
     */
    @Test
    void getAllStudents_ShouldReturnAllStudents() {
        // Arrange
        when(studentRepository.findAll()).thenReturn(testStudents);

        // Act
        List<Student> result = studentService.getAllStudents();

        // Assert
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
        verify(studentRepository, times(1)).findAll();
    }

    /**
     * Test that getStudentById returns the correct student when found
     */
    @Test
    void getStudentById_WhenStudentExists_ShouldReturnStudent() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));

        // Act
        Optional<Student> result = studentService.getStudentById(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("John", result.get().getFirstName());
        verify(studentRepository, times(1)).findById(1L);
    }

    /**
     * Test that getStudentById returns empty when student is not found
     */
    @Test
    void getStudentById_WhenStudentDoesNotExist_ShouldReturnEmpty() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act
        Optional<Student> result = studentService.getStudentById(99L);

        // Assert
        assertFalse(result.isPresent());
        verify(studentRepository, times(1)).findById(99L);
    }

    /**
     * Test that createStudent saves the student and returns it
     */
    @Test
    void createStudent_ShouldSaveAndReturnStudent() {
        // Arrange
        Student newStudent = new Student("New", "Student", "new.student@example.com", 25);
        when(studentRepository.save(any(Student.class))).thenReturn(newStudent);

        // Act
        Student result = studentService.createStudent(newStudent);

        // Assert
        assertEquals("New", result.getFirstName());
        verify(studentRepository, times(1)).save(newStudent);
    }

    /**
     * Test that updateStudent updates an existing student
     */
    @Test
    void updateStudent_WhenStudentExists_ShouldUpdateAndReturnStudent() {
        // Arrange
        Student updatedDetails = new Student("Updated", "Student", "updated@example.com", 30);
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Student result = studentService.updateStudent(1L, updatedDetails);

        // Assert
        assertEquals("Updated", result.getFirstName());
        assertEquals("Student", result.getLastName());
        assertEquals("updated@example.com", result.getEmail());
        assertEquals(30, result.getAge());
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).save(any(Student.class));
    }

    /**
     * Test that updateStudent throws exception when student does not exist
     */
    @Test
    void updateStudent_WhenStudentDoesNotExist_ShouldThrowException() {
        // Arrange
        Student updatedDetails = new Student("Updated", "Student", "updated@example.com", 30);
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            studentService.updateStudent(99L, updatedDetails);
        });
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).save(any(Student.class));
    }

    /**
     * Test that deleteStudent deletes an existing student
     */
    @Test
    void deleteStudent_WhenStudentExists_ShouldDeleteStudent() {
        // Arrange
        when(studentRepository.findById(1L)).thenReturn(Optional.of(testStudent));
        doNothing().when(studentRepository).delete(any(Student.class));

        // Act
        studentService.deleteStudent(1L);

        // Assert
        verify(studentRepository, times(1)).findById(1L);
        verify(studentRepository, times(1)).delete(testStudent);
    }

    /**
     * Test that deleteStudent throws exception when student does not exist
     */
    @Test
    void deleteStudent_WhenStudentDoesNotExist_ShouldThrowException() {
        // Arrange
        when(studentRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EntityNotFoundException.class, () -> {
            studentService.deleteStudent(99L);
        });
        verify(studentRepository, times(1)).findById(99L);
        verify(studentRepository, never()).delete(any(Student.class));
    }

    /**
     * Test that findStudentsByLastName returns students with matching last name
     */
    @Test
    void findStudentsByLastName_ShouldReturnMatchingStudents() {
        // Arrange
        when(studentRepository.findByLastName("Doe")).thenReturn(List.of(testStudent));

        // Act
        List<Student> result = studentService.findStudentsByLastName("Doe");

        // Assert
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        verify(studentRepository, times(1)).findByLastName("Doe");
    }

    /**
     * Test that findStudentsByMinimumAge returns students with age greater than specified
     */
    @Test
    void findStudentsByMinimumAge_ShouldReturnMatchingStudents() {
        // Arrange
        Student olderStudent = new Student("Older", "Person", "older@example.com", 25);
        when(studentRepository.findByAgeGreaterThan(21)).thenReturn(List.of(olderStudent));

        // Act
        List<Student> result = studentService.findStudentsByMinimumAge(21);

        // Assert
        assertEquals(1, result.size());
        assertEquals("Older", result.get(0).getFirstName());
        assertEquals(25, result.get(0).getAge());
        verify(studentRepository, times(1)).findByAgeGreaterThan(21);
    }
} 