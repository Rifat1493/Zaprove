package com.jamiur.disbursement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class DisbursementApplication {

	public static void main(String[] args) {
		SpringApplication.run(DisbursementApplication.class, args);
	}

}
