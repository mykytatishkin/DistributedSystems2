package com.distributed.homework.controller;

import com.distributed.homework.model.Student;
import com.distributed.homework.service.StudentService;
import com.distributed.homework.service.XmlService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the StudentXmlController class
 * <p>
 * These tests verify that the controller correctly interacts with services
 * and handles various scenarios for XML export operations.
 * </p>
 * 
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
@ExtendWith(MockitoExtension.class)
public class StudentXmlControllerTest {

    @Mock
    private StudentService studentService;

    @Mock
    private XmlService xmlService;

    @InjectMocks
    private StudentXmlController studentXmlController;

    private List<Student> testStudents;

    /**
     * Setup test data before each test
     */
    @BeforeEach
    void setUp() {
        Student student1 = new Student("John", "Doe", "john.doe@example.com", 20);
        student1.setId(1L);
        
        Student student2 = new Student("Jane", "Smith", "jane.smith@example.com", 22);
        student2.setId(2L);
        
        testStudents = Arrays.asList(student1, student2);
    }

    /**
     * Test that exportAllStudents returns the path to the saved XML file
     */
    @Test
    void exportAllStudents_ShouldReturnFilePath() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(testStudents);
        when(xmlService.objectToXmlFile(any(), anyString())).thenReturn("/path/to/students.xml");

        // Act
        ResponseEntity<String> response = studentXmlController.exportAllStudents();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("/path/to/students.xml"));
    }

    /**
     * Test that exportAllStudents handles exceptions gracefully
     */
    @Test
    void exportAllStudents_WhenExceptionOccurs_ShouldReturnErrorMessage() throws Exception {
        // Arrange
        when(studentService.getAllStudents()).thenReturn(testStudents);
        when(xmlService.objectToXmlFile(any(), anyString())).thenThrow(new RuntimeException("Test error"));

        // Act
        ResponseEntity<String> response = studentXmlController.exportAllStudents();

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error exporting students"));
    }

    /**
     * Test that exportStudentsByLastName returns the path to the saved XML file
     */
    @Test
    void exportStudentsByLastName_ShouldReturnFilePath() throws Exception {
        // Arrange
        when(studentService.findStudentsByLastName("Doe")).thenReturn(Collections.singletonList(testStudents.get(0)));
        when(xmlService.objectToXmlFile(any(), anyString())).thenReturn("/path/to/doe-students.xml");

        // Act
        ResponseEntity<String> response = studentXmlController.exportStudentsByLastName("Doe");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("/path/to/doe-students.xml"));
    }

    /**
     * Test that exportStudentsByMinimumAge returns the path to the saved XML file
     */
    @Test
    void exportStudentsByMinimumAge_ShouldReturnFilePath() throws Exception {
        // Arrange
        when(studentService.findStudentsByMinimumAge(21)).thenReturn(Collections.singletonList(testStudents.get(1)));
        when(xmlService.objectToXmlFile(any(), anyString())).thenReturn("/path/to/age-students.xml");

        // Act
        ResponseEntity<String> response = studentXmlController.exportStudentsByMinimumAge(21);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("/path/to/age-students.xml"));
    }
} 