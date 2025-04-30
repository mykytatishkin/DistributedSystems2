package com.distributed.homework.service;

import com.distributed.homework.model.Student;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        MockitoAnnotations.openMocks(this);
        // Set the storage path to a temporary directory
        ReflectionTestUtils.setField(xmlService, "storagePath", tempDir.toString());
    }

    /**
     * Test that objectToXmlFile correctly creates an XML file and returns its path
     */
    @Test
    void objectToXmlFile_ShouldCreateFileAndReturnPath() throws Exception {
        // Arrange
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
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
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        
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
        Student student = new Student("John", "Doe", "john@example.com", 
            "+1234567890", "123 Main St", "Computer Science");
        
        // Mock the marshaller behavior
        doAnswer(invocation -> null).when(jaxb2Marshaller).marshal(any(), any(StreamResult.class));

        // Act - call twice with the same base filename
        String result1 = xmlService.objectToXmlFile(student, "same-name");
        String result2 = xmlService.objectToXmlFile(student, "same-name");

        // Assert
        assertNotEquals(result1, result2);
    }

    @Test
    void parseXmlFile() throws IOException {
        // Arrange
        String xmlContent = """
            <?xml version="1.0" encoding="UTF-8"?>
            <students>
                <student>
                    <firstName>John</firstName>
                    <lastName>Doe</lastName>
                    <email>john@example.com</email>
                    <phoneNumber>+1234567890</phoneNumber>
                    <address>123 Main St</address>
                    <major>Computer Science</major>
                </student>
                <student>
                    <firstName>Jane</firstName>
                    <lastName>Smith</lastName>
                    <email>jane@example.com</email>
                    <phoneNumber>+0987654321</phoneNumber>
                    <address>456 Oak Ave</address>
                    <major>Mathematics</major>
                </student>
            </students>
            """;
        MultipartFile file = new MockMultipartFile("students.xml", xmlContent.getBytes());

        // Act
        List<Student> students = xmlService.parseXmlFile(file);

        // Assert
        assertEquals(2, students.size());
        assertEquals("John", students.get(0).getFirstName());
        assertEquals("Doe", students.get(0).getLastName());
        assertEquals("john@example.com", students.get(0).getEmail());
        assertEquals("+1234567890", students.get(0).getPhoneNumber());
        assertEquals("123 Main St", students.get(0).getAddress());
        assertEquals("Computer Science", students.get(0).getMajor());
    }

    @Test
    void generateXml() {
        // Arrange
        List<Student> students = Arrays.asList(
            new Student("John", "Doe", "john@example.com", 
                "+1234567890", "123 Main St", "Computer Science"),
            new Student("Jane", "Smith", "jane@example.com", 
                "+0987654321", "456 Oak Ave", "Mathematics")
        );

        // Act
        String xml = xmlService.generateXml(students);

        // Assert
        assertTrue(xml.contains("<firstName>John</firstName>"));
        assertTrue(xml.contains("<lastName>Doe</lastName>"));
        assertTrue(xml.contains("<email>john@example.com</email>"));
        assertTrue(xml.contains("<phoneNumber>+1234567890</phoneNumber>"));
        assertTrue(xml.contains("<address>123 Main St</address>"));
        assertTrue(xml.contains("<major>Computer Science</major>"));
    }

    @Test
    void parseXmlFile_InvalidXml() {
        // Arrange
        String invalidXml = "invalid xml content";
        MultipartFile file = new MockMultipartFile("invalid.xml", invalidXml.getBytes());

        // Act & Assert
        assertThrows(IOException.class, () -> xmlService.parseXmlFile(file));
    }
} 