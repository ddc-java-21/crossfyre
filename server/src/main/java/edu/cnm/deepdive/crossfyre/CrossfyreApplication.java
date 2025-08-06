package edu.cnm.deepdive.crossfyre;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main entry point for the Crossfyre Spring Boot application.
 *
 * <p>This class bootstraps the application context and enables scheduling support
 * for any scheduled tasks within the application.</p>
 */
@SpringBootApplication
@EnableScheduling
public class CrossfyreApplication {

	/**
	 * Starts the Crossfyre application.
	 *
	 * @param args command line arguments (not used).
	 */
	public static void main(String[] args) {
		SpringApplication.run(CrossfyreApplication.class, args);
	}
}
