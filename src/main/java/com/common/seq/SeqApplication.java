package com.common.seq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.common.seq.config.ProfileManager;
import com.common.seq.config.env.EnvConfiguration;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootApplication
public class SeqApplication {

	public SeqApplication(EnvConfiguration envConfiguration, ProfileManager profileManager) {
		log.info(envConfiguration.getMessage());
		profileManager.getActiveProfiles();
	}

	public static void main(String[] args) {
		SpringApplication.run(SeqApplication.class, args);
	}
}
