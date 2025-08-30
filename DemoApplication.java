package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class DemoApplication implements CommandLineRunner {

	private final RestTemplate restTemplate = new RestTemplate();

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) {
		try {
			// Step 1: Generate Webhook
			String generateWebhookUrl = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

			Map<String, String> requestBody = new HashMap<>();
			requestBody.put("name", "PYDISETTI SRI CHARAN");
			requestBody.put("regNo", "22BCE9761");
			requestBody.put("email", "sricharanpydisetti33@gmail.com");

			ResponseEntity<WebhookResponse> response =
					restTemplate.postForEntity(generateWebhookUrl, requestBody, WebhookResponse.class);

			if (response.getBody() == null) {
				System.out.println("Failed to generate webhook. Response body is null.");
				return;
			}

			WebhookResponse webhookResponse = response.getBody();
			String webhookUrl = webhookResponse.getWebhook();
			String accessToken = webhookResponse.getAccessToken();

			// Step 2: SQL Query (answer for SQL1 - based on odd registration number)
			String finalQuery =
					"SELECT p.AMOUNT AS SALARY, " +
							"CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, " +
							"TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, " +
							"d.DEPARTMENT_NAME " +
							"FROM PAYMENTS p " +
							"JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID " +
							"JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID " +
							"WHERE DAY(p.PAYMENT_TIME) <> 1 " +
							"ORDER BY p.AMOUNT DESC " +
							"LIMIT 1";

			// Step 3: Prepare headers with Authorization
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", accessToken);

			// Step 4: Prepare body
			Map<String, String> sqlBody = new HashMap<>();
			sqlBody.put("finalQuery", finalQuery);

			HttpEntity<Map<String, String>> entity = new HttpEntity<>(sqlBody, headers);

			// Step 5: Submit
			ResponseEntity<String> submission =
					restTemplate.postForEntity(webhookUrl, entity, String.class);

			System.out.println("Submission completed successfully.");

		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
}