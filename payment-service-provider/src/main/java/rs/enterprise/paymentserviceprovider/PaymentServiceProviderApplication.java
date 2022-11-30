package rs.enterprise.paymentserviceprovider;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableFeignClients
@EnableMongoRepositories
@EnableAsync
public class PaymentServiceProviderApplication {

	public static void main(String[] args) {
		SpringApplication.run(PaymentServiceProviderApplication.class, args);
	}

}
