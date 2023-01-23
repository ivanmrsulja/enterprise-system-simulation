package rs.enterprise.paymentserviceprovider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@Configuration
public class BeanConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecretKey secretKey() {
        var secureRandom = new SecureRandom();

        KeyGenerator keygenerator;
        try {
            keygenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            return null; // If this ever returns null, check dependencies and rebuild the project.
        }

        keygenerator.init(256, secureRandom);
        return keygenerator.generateKey();
    }

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://127.0.0.1:5173", "http://localhost:5173", "http://localhost:8080", "http://localhost:8081", "http://www.bonita-sajt.com:8443", "http://www.bonita-sajt.com:8080")
                        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }
}
