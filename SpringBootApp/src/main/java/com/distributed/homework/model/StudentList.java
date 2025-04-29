package com.distributed.homework.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Wrapper class for a list of students to properly marshal to XML
 * <p>
 * This class is needed for JAXB to properly marshal a list of students to XML
 * with a proper root element.
 * </p>
 *
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
@XmlRootElement(name = "students")
@XmlAccessorType(XmlAccessType.FIELD)
public class StudentList {
    
    @XmlElement(name = "student")
    private List<Student> students;

    /**
     * Default constructor required by JAXB
     */
    public StudentList() {
    }

    /**
     * Create a new StudentList with the provided list of students
     * 
     * @param students the list of students
     */
    public StudentList(List<Student> students) {
        this.students = students;
    }

    /**
     * Get the list of students
     * 
     * @return the list of students
     */
    public List<Student> getStudents() {
        return students;
    }

    /**
     * Set the list of students
     * 
     * @param students the list of students to set
     */
    public void setStudents(List<Student> students) {
        this.students = students;
    }
} 