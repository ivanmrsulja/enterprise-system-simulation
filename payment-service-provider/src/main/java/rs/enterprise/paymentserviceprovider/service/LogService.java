package rs.enterprise.paymentserviceprovider.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rs.enterprise.paymentserviceprovider.model.enums.LogType;
import rs.enterprise.paymentserviceprovider.model.valueobjects.Log;
import rs.enterprise.paymentserviceprovider.repository.mongo.LogRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogService(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    public Log info(String ipAddress, String message, String component, String username, List<String> methodParams) {
        return this.saveLog(ipAddress, message, component, LogType.INFO, username, methodParams);

    }

    public Log error(String ipAddress, String message, String component, String username, List<String> methodParams) {
        return this.saveLog(ipAddress, message, component, LogType.ERROR, username, methodParams);

    }

    public Log debug(String ipAddress, String message, String component, String username, List<String> methodParams) {
        return this.saveLog(ipAddress, message, component, LogType.DEBUG, username, methodParams);

    }

    public Log warn(String ipAddress, String message, String component, String username, List<String> methodParams) {
        return this.saveLog(ipAddress, message, component, LogType.WARN, username, methodParams);
    }

    private Log saveLog(String ipAddress, String message, String component, LogType type, String username, List<String> methodParams) {
        var log = new Log();
        log.setLogType(type);
        log.setTimestamp(LocalDateTime.now());
        log.setIpAddress(ipAddress);
        log.setMessage(message);
        log.setComponent(component);
        log.setMethodParams(methodParams);
        log.setUsername(username);
        return this.logRepository.save(log);
    }
}
