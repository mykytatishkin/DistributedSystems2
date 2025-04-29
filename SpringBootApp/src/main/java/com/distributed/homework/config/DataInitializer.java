package com.distributed.homework.config;

import com.distributed.homework.model.Student;
import com.distributed.homework.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Initialize sample data for testing purposes
 */
@Component
public class DataInitializer implements CommandLineRunner {

    private final StudentRepository studentRepository;

    @Autowired
    public DataInitializer(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        // Add some sample data only if repository is empty
        if (studentRepository.count() == 0) {
            studentRepository.save(new Student("John", "Doe", "john.doe@example.com", 20));
            studentRepository.save(new Student("Jane", "Smith", "jane.smith@example.com", 22));
            studentRepository.save(new Student("Bob", "Johnson", "bob.johnson@example.com", 21));
            studentRepository.save(new Student("Alice", "Brown", "alice.brown@example.com", 19));
            studentRepository.save(new Student("Charlie", "Davis", "charlie.davis@example.com", 23));
            
            System.out.println("Sample data initialized successfully!");
        }
    }
} 