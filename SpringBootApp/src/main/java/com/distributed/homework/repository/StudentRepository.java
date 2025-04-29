package com.distributed.homework.repository;

import com.distributed.homework.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

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
     * Find students with age greater than the specified value
     * @param age the minimum age
     * @return list of matching students
     */
    List<Student> findByAgeGreaterThan(int age);
} 