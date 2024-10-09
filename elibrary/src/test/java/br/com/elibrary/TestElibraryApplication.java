package br.com.elibrary;

import org.springframework.boot.SpringApplication;

public class TestElibraryApplication {

	public static void main(String[] args) {
		SpringApplication.from(ElibraryApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
