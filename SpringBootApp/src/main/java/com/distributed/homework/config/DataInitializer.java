package com.distributed.homework.config;

import com.distributed.homework.model.Student;
import com.distributed.homework.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Initialize sample data for testing purposes
 */
@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(StudentRepository studentRepository) {
        return args -> {
            // Create some initial students
            Student student1 = new Student("John", "Doe", "john@example.com", 
                "+1234567890", "123 Main St", "Computer Science");
            Student student2 = new Student("Jane", "Smith", "jane@example.com", 
                "+0987654321", "456 Oak Ave", "Mathematics");
            Student student3 = new Student("Bob", "Johnson", "bob@example.com", 
                "+1122334455", "789 Pine St", "Physics");

            studentRepository.save(student1);
            studentRepository.save(student2);
            studentRepository.save(student3);
        };
    }
} 