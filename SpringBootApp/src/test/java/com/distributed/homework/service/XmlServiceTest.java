package com.distributed.homework.service;

import com.distributed.homework.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.util.ReflectionTestUtils;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;

/**
 * Unit tests for the XmlService class
 * <p>
 * These tests verify that the service correctly transforms objects to XML
 * and saves them to the filesystem.
 * </p>
 * 
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
@ExtendWith(MockitoExtension.class)
public class XmlServiceTest {

    @Mock
    private Jaxb2Marshaller jaxb2Marshaller;

    @InjectMocks
    private XmlService xmlService;

    @TempDir
    Path tempDir;

    /**
     * Setup test environment before each test
     */
    @BeforeEach
    void setUp() {
        // Set the storage path to a temporary directory
        ReflectionTestUtils.setField(xmlService, "storagePath", tempDir.toString());
    }

    /**
     * Test that objectToXmlFile correctly creates an XML file and returns its path
     */
    @Test
    void objectToXmlFile_ShouldCreateFileAndReturnPath() throws Exception {
        // Arrange
        Student student = new Student("John", "Doe", "john.doe@example.com", 20);
        List<Student> students = Arrays.asList(student);
        
        // Mock the marshaller behavior
        doAnswer(invocation -> {
            // Simulate writing to the file
            StreamResult result = invocation.getArgument(1);
            assertNotNull(result);
            return null;
        }).when(jaxb2Marshaller).marshal(any(), any(StreamResult.class));

        // Act
        String result = xmlService.objectToXmlFile(students, "test-students");

        // Assert
        assertNotNull(result);
        assertTrue(result.endsWith(".xml"));
        assertTrue(result.contains("test-students"));
        
        // Verify the file exists
        File file = new File(result);
        assertTrue(file.exists());
        assertTrue(file.isFile());
    }

    /**
     * Test that objectToXmlFile creates the directory if it doesn't exist
     */
    @Test
    void objectToXmlFile_WhenDirectoryDoesNotExist_ShouldCreateDirectory() throws Exception {
        // Arrange
        Student student = new Student("John", "Doe", "john.doe@example.com", 20);
        
        // Set storage path to a non-existent directory
        String nonExistentDir = tempDir.resolve("non-existent").toString();
        ReflectionTestUtils.setField(xmlService, "storagePath", nonExistentDir);
        
        // Mock the marshaller behavior
        doAnswer(invocation -> null).when(jaxb2Marshaller).marshal(any(), any(StreamResult.class));

        // Act
        String result = xmlService.objectToXmlFile(student, "test");

        // Assert
        File directory = new File(nonExistentDir);
        assertTrue(directory.exists());
        assertTrue(directory.isDirectory());
    }

    /**
     * Test that objectToXmlFile generates a unique filename
     */
    @Test
    void objectToXmlFile_ShouldGenerateUniqueFilename() throws Exception {
        // Arrange
        Student student = new Student("John", "Doe", "john.doe@example.com", 20);
        
        // Mock the marshaller behavior
        doAnswer(invocation -> null).when(jaxb2Marshaller).marshal(any(), any(StreamResult.class));

        // Act - call twice with the same base filename
        String result1 = xmlService.objectToXmlFile(student, "same-name");
        String result2 = xmlService.objectToXmlFile(student, "same-name");

        // Assert
        assertNotEquals(result1, result2);
    }
} 