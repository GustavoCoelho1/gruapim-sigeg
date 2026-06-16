package com.sigeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SigegApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigegApplication.class, args);
		System.out.println("Aplicação SIGEG iniciada com sucesso!");
	}

}
