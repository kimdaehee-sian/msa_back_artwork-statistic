package com.example.artwork_statistics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class ArtworkStatisticsApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(ArtworkStatisticsApplication.class, args);
		
		// 등록된 컨트롤러 확인
		String[] controllerBeans = context.getBeanNamesForAnnotation(RestController.class);
		log.info("=== Registered Controllers ===");
		for (String bean : controllerBeans) {
			log.info("Controller: {}", bean);
		}
		log.info("=== Total Controllers: {} ===", controllerBeans.length);
	}

}
