package com.example.vk_bot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VkBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(VkBotApplication.class, args);
	}

}
