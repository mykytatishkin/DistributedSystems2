package com.distributed.homework.controller;

import com.distributed.homework.model.Student;
import com.distributed.homework.model.StudentList;
import com.distributed.homework.service.StudentService;
import com.distributed.homework.service.XmlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * REST Controller for Student XML export functionality
 * <p>
 * This controller provides endpoints that filter student data based on query parameters,
 * transform the filtered data to XML, save it to a local directory, and return the path
 * to the saved XML file.
 * </p>
 *
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
@RestController
@RequestMapping("/api/students/export")
public class StudentXmlController {

    private final StudentService studentService;
    private final XmlService xmlService;

    /**
     * Constructs a new StudentXmlController with the specified services
     *
     * @param studentService Service for student data operations
     * @param xmlService Service for XML transformation and storage
     */
    @Autowired
    public StudentXmlController(StudentService studentService, XmlService xmlService) {
        this.studentService = studentService;
        this.xmlService = xmlService;
    }

    /**
     * Exports all students to an XML file
     *
     * @return The path to the saved XML file
     */
    @GetMapping("/all")
    public ResponseEntity<String> exportAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            String filePath = xmlService.objectToXmlFile(new StudentList(students), "all-students");
            return ResponseEntity.ok("XML file saved to: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error exporting students: " + e.getMessage());
        }
    }

    /**
     * Exports students filtered by last name to an XML file
     *
     * @param lastName The last name to filter by
     * @return The path to the saved XML file
     */
    @GetMapping(params = "lastName")
    public ResponseEntity<String> exportStudentsByLastName(@RequestParam String lastName) {
        try {
            List<Student> students = studentService.findStudentsByLastName(lastName);
            String filePath = xmlService.objectToXmlFile(
                    new StudentList(students), 
                    "students-by-lastname-" + lastName);
            return ResponseEntity.ok("XML file saved to: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error exporting students: " + e.getMessage());
        }
    }

    /**
     * Exports students filtered by minimum age to an XML file
     *
     * @param minAge The minimum age to filter by
     * @return The path to the saved XML file
     */
    @GetMapping(params = "minAge")
    public ResponseEntity<String> exportStudentsByMinimumAge(@RequestParam int minAge) {
        try {
            List<Student> students = studentService.findStudentsByMinimumAge(minAge);
            String filePath = xmlService.objectToXmlFile(
                    new StudentList(students), 
                    "students-by-minage-" + minAge);
            return ResponseEntity.ok("XML file saved to: " + filePath);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error exporting students: " + e.getMessage());
        }
    }
} 