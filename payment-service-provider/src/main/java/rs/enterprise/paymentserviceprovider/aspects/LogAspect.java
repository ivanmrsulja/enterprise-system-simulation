package rs.enterprise.paymentserviceprovider.aspects;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import rs.enterprise.paymentserviceprovider.annotation.Log;
import rs.enterprise.paymentserviceprovider.service.LogService;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@Slf4j
public class LogAspect {

    private final LogService logService;

    @Autowired
    public LogAspect(LogService logService) {
        this.logService = logService;
    }

    private List<String> getMethodParams(Object[] args) {
        List<String> params = new ArrayList<>();
        for (int i = 1; i < args.length; i++) {
            params.add(args[i].toString());
        }
        return params;
    }

    private String getUsername() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null)
            return "Not authenticated";
        return authentication.getName();
    }

    private Log getAnnotation(JoinPoint joinPoint) {
        var signature = (MethodSignature) joinPoint.getSignature();
        var method = signature.getMethod();
        return method.getAnnotation(Log.class);
    }


    @AfterReturning("@annotation(rs.enterprise.paymentserviceprovider.annotation.Log)")
    public void logInfo(JoinPoint joinPoint) {
        var args = joinPoint.getArgs();
        var request = (HttpServletRequest) args[0];
        var annotation = this.getAnnotation(joinPoint);
        String username = this.getUsername();

        List<String> params = this.getMethodParams(args);

        String methodDetails = joinPoint.getStaticPart().toString();

        String component = methodDetails.substring(10, methodDetails.length() - 1);

        this.logService.info(request.getRemoteAddr(), annotation.message(), component, username, params);
    }

    @AfterThrowing(value = "@annotation(rs.enterprise.paymentserviceprovider.annotation.Log)", throwing = "exception")
    public void logError(JoinPoint joinPoint, Exception exception) {
        var args = joinPoint.getArgs();
        var request = (HttpServletRequest) args[0];
        String methodDetails = joinPoint.getStaticPart().toString();
        String component = methodDetails.substring(10, methodDetails.length() - 1);

        String username = this.getUsername();

        List<String> params = this.getMethodParams(args);


        var stacktrace = exception.getStackTrace();
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder
                .append(exception.getClass().toString())
                .append(": ")
                .append(exception.getMessage())
                .append(System.lineSeparator());

        for (var elem : stacktrace) {
            stringBuilder.append(elem.toString()).append(System.lineSeparator());
        }

        this.logService.error(request.getRemoteAddr(), stringBuilder.toString(), component, username, params);
    }
}
