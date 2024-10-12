package com.demo.ormtuningapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class OrmTunningApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrmTunningApiApplication.class, args);
	}

}
