package it.erroridiprezzo.ErroriDiPrezzoShort.configuration;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.MultipartConfigElement;
import java.io.File;


@Configuration
public class MultipartConfig {

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        File tmpFile = new File("src/main/resources/tmp");
        if (!tmpFile.exists()) {
            if ( tmpFile.mkdirs() ) {
                System.out.println("tmp dir created");
            }
        }
        factory.setLocation(tmpFile.getAbsolutePath());
        return factory.createMultipartConfig();
    }

}
