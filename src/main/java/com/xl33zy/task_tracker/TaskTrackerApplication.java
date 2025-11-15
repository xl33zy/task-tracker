package com.xl33zy.task_tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TaskTrackerApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskTrackerApplication.class, args);
	}
}
