package br.edu.ifmg.produto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ProdutoApplication {

	public static void main(String[] args) {

		System.out.println("Aplicação iniciando...");
		SpringApplication.run(ProdutoApplication.class, args);
	}

}


