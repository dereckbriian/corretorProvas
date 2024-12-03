package br.com.corretorProvas;

import br.com.corretorProvas.service.ServidorService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
@SpringBootApplication
public class CorretorProvasApplication {

	public static void main(String[] args) {
		SpringApplication.run(CorretorProvasApplication.class, args);
	}

	@SpringBootApplication
	public static class CorrecaoApplication {

		public static void main(String[] args) {
			SpringApplication.run(CorrecaoApplication.class, args);
		}

		@Bean
		public CommandLineRunner run(ServidorService servidorService) {
			return args -> {
				servidorService.iniciarServidor(); // Inicia o servidor TCP
			};
		}
	}
}
