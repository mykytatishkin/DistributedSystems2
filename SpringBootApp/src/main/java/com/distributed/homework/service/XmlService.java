package com.distributed.homework.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

/**
 * Service responsible for transforming objects to XML and saving them to the filesystem
 * <p>
 * This service uses JAXB to marshal Java objects to XML representation and saves
 * the resulting XML files to a configured directory on the local filesystem.
 * </p>
 * 
 * @author Distributed Systems Student
 * @version 1.0
 * @since 2025-04-21
 */
@Service
public class XmlService {

    private final Jaxb2Marshaller jaxb2Marshaller;
    
    @Value("${app.xml.storage-path:xml-output}")
    private String storagePath;

    /**
     * Constructs a new XmlService with the specified marshaller
     * 
     * @param jaxb2Marshaller The JAXB marshaller to use for XML transformation
     */
    public XmlService(Jaxb2Marshaller jaxb2Marshaller) {
        this.jaxb2Marshaller = jaxb2Marshaller;
    }

    /**
     * Transforms an object to XML and saves it to the local filesystem
     * 
     * @param object The object to transform and save
     * @param filename Base filename to use (will be appended with UUID)
     * @return The absolute path to the saved XML file
     * @throws Exception If any error occurs during XML transformation or file operations
     */
    public String objectToXmlFile(Object object, String filename) throws Exception {
        // Create directory if it doesn't exist
        File directory = new File(storagePath);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Generate unique filename
        String uniqueFilename = filename + "-" + UUID.randomUUID().toString() + ".xml";
        Path filePath = Paths.get(directory.getAbsolutePath(), uniqueFilename);
        File outputFile = filePath.toFile();

        // Marshal object to XML file
        try (FileOutputStream os = new FileOutputStream(outputFile)) {
            jaxb2Marshaller.marshal(object, new StreamResult(os));
        }

        return outputFile.getAbsolutePath();
    }
} 