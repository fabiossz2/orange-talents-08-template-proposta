package br.com.zupacademy.fabio.propostas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class PropostasApplication {

	public static void main(String[] args) {
		SpringApplication.run(PropostasApplication.class, args);
	}

}
