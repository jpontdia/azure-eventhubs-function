package com.demo.logging;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@Slf4j
@SuppressWarnings("unused")
public class FunctionApplication implements CommandLineRunner {

	@Autowired
	BuildProperties buildProperties;

	@Autowired
	String componentVersion;

	public static void main(String[] args) {
		SpringApplication.run(FunctionApplication.class, args);
		}

	@Bean
	public String componentVersion(){
		return buildProperties.getGroup()
				+ ":" + buildProperties.getArtifact()
				+ ":" +  buildProperties.getName()
				+ ":" +  buildProperties.getVersion()
				+ ", " + buildProperties.getTime();
	}

	@Override
	public void run(String... args) {
		log.info("Application version: {}", componentVersion);
	}
}
