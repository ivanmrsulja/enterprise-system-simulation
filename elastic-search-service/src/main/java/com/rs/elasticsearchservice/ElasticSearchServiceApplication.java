package com.rs.elasticsearchservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ElasticSearchServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElasticSearchServiceApplication.class, args);
	}

}
