package com.distributed.homework.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.xml.MarshallingHttpMessageConverter;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

/**
 * Configuration class for XML marshalling/unmarshalling
 */
@Configuration
public class XmlConfig {

    /**
     * Configure JAXB marshaller for XML conversion
     */
    @Bean
    public Jaxb2Marshaller jaxb2Marshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        // Include both model and controller packages for JAXB scanning
        marshaller.setPackagesToScan("com.distributed.homework.model", "com.distributed.homework.controller");
        return marshaller;
    }

    /**
     * Configure HTTP message converter for XML
     */
    @Bean
    public MarshallingHttpMessageConverter marshallingHttpMessageConverter(Jaxb2Marshaller jaxb2Marshaller) {
        MarshallingHttpMessageConverter converter = new MarshallingHttpMessageConverter();
        converter.setMarshaller(jaxb2Marshaller);
        converter.setUnmarshaller(jaxb2Marshaller);
        return converter;
    }
} 