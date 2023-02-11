package com.rs.elasticsearchservice.aspect;

import com.rs.elasticsearchservice.exception.BadApiKeyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

@Aspect
@Component
public class ApiKeyAspect {

    @Value("${API_KEY}")
    private String apiKey;

    @Around("@annotation(com.rs.elasticsearchservice.annotation.CheckApiKey)")
    public Object checkApiKey(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(
                RequestContextHolder.getRequestAttributes())).getRequest();

        String acquiredApiKey = request.getHeader("X-API-KEY");
        if(!apiKey.equals(acquiredApiKey)) {
            throw new BadApiKeyException("Bad api key.");
        }

        return joinPoint.proceed();
    }
}
