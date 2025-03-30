package com.slf.zmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.slf.zmt.repository")
public class ZmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(ZmtApplication.class, args);
	}

}
