package com.sigeg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * SIGEG – Sistema Integrado de Gestão de Entregas Gastronômicas
 * Grupo: Gustavo Coelho, Hugo Barbosa, Luis Fernando
 */
@SpringBootApplication
@EnableConfigurationProperties
@EnableJpaAuditing
public class SigegApplication {

	public static void main(String[] args) {
		SpringApplication.run(SigegApplication.class, args);
		System.out.println("Aplicação SIGEG iniciada com sucesso!");
	}
}
