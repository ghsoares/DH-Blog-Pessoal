package com.blogpessoal.blogpessoal.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import com.blogpessoal.blogpessoal.model.UsuarioModel;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UsuarioRepositoryTest {

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();

		usuarioRepository.save(new UsuarioModel(0L, "Zé Colmeia", "roba_lance@email.com", "12345678",
				"https://image1.com/seilaonde.jpg"));
		usuarioRepository.save(
				new UsuarioModel(0L, "Luana Colmeia", "luana_tenguan@email.com", "12345678", "https://image1.com/luana.jpg"));
		usuarioRepository.save(new UsuarioModel(0L, "Guilherme Colmeia", "fuba_cremoso@email.com", "12345678",
				"https://image1.com/estouesperando.jpg"));
		usuarioRepository.save(new UsuarioModel(0L, "Jefferson da Silva Souza", "jeff_silva@email.com", "12345678",
				"https://image1.com/jefferson.jpg"));

	}

	@Test
	@DisplayName("Retorna 1 usuário")
	public void deveRetornarUmUsuario() {
		Optional<UsuarioModel> usuario = usuarioRepository.findByUsuario("luana_tenguan@email.com");

		assertTrue(usuario.get().getUsuario().equals("luana_tenguan@email.com"));
	}

	@Test
	@DisplayName("Retorna 3 usuários")
	public void deveRetornarTresUsuario() {
		List<UsuarioModel> listaDeUsuarios = usuarioRepository.findAllByNomeContainingIgnoreCase("Colmeia");

		assertEquals(3, listaDeUsuarios.size());
		assertTrue(listaDeUsuarios.get(0).getNome().equals("Zé Colmeia"));
		assertTrue(listaDeUsuarios.get(1).getNome().equals("Luana Colmeia"));
		assertTrue(listaDeUsuarios.get(2).getNome().equals("Guilherme Colmeia"));
	}
}








