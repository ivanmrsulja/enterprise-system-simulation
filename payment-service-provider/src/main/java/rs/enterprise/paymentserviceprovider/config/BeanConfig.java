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
            return null;
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
                        .allowedOrigins("http://127.0.0.1:5173")
                        .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
            }
        };
    }
}
