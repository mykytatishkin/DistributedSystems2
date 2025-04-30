package com.distributed.homework.repository;

import com.distributed.homework.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Student entities
 */
@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    
    /**
     * Find students by their last name
     * @param lastName the last name to search for
     * @return list of matching students
     */
    List<Student> findByLastName(String lastName);
    
    /**
     * Find students by their department
     * @param department the department to search for
     * @return list of matching students
     */
    List<Student> findByDepartment(String department);
    
    /**
     * Find a student by their email
     * @param email the email to search for
     * @return the student with the specified email
     */
    Optional<Student> findByEmail(String email);
    
    /**
     * Find students by their phone number
     * @param phoneNumber the phone number to search for
     * @return list of matching students
     */
    List<Student> findByPhoneNumber(String phoneNumber);
    
    /**
     * Find students by their address
     * @param address the address to search for
     * @return list of matching students
     */
    List<Student> findByAddressContaining(String address);
} 