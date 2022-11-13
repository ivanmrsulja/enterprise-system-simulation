package rs.enterprise.paymentserviceprovider.model.valueobjects;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import rs.enterprise.paymentserviceprovider.model.enums.LogType;

import javax.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "logs")
@Getter
@Setter
public class Log {

    @Id
    private String id;

    private String message;

    private LocalDateTime timestamp;

    private String ipAddress;

    private LogType logType;

    private String component;

    private List<String> methodParams;

    private String username;


    public Log() {
        this.methodParams = new ArrayList<>();
    }

    public Log(String message, LocalDateTime timestamp, String ipAddress, LogType logType, String component) {
        this.message = message;
        this.timestamp = timestamp;
        this.ipAddress = ipAddress;
        this.logType = logType;
        this.component = component;
        this.methodParams = new ArrayList<>();
    }


    public void addMethodParam(String param) {
        this.methodParams.add(param);
    }
}