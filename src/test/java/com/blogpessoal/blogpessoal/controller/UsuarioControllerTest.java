package com.blogpessoal.blogpessoal.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.blogpessoal.blogpessoal.model.UsuarioModel;
import com.blogpessoal.blogpessoal.repository.UsuarioRepository;
import com.blogpessoal.blogpessoal.service.UsuarioService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsuarioControllerTest {

	@Autowired
	private TestRestTemplate testRestTemplate;

	@Autowired
	private UsuarioService usuarioService;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@BeforeAll
	void start() {
		usuarioRepository.deleteAll();
	}

	@Test
	@Order(1)
	@DisplayName("Cadastrar um usuário")
	public void deveCadastrarUmUsuario() {
		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(new UsuarioModel(0L, "Gabriel Henrique",
				"gabriel_henrique@gmail.com", "12345678", "http://fotolegal.jpg"));

		ResponseEntity<UsuarioModel> resposta = testRestTemplate.exchange("/usuarios/register", HttpMethod.POST,
				requisicao, UsuarioModel.class);

		assertEquals(HttpStatus.CREATED, resposta.getStatusCode());
		assertEquals(requisicao.getBody().getNome(), resposta.getBody().getNome());
		assertEquals(requisicao.getBody().getUsuario(), resposta.getBody().getUsuario());
		assertEquals(requisicao.getBody().getFoto(), resposta.getBody().getFoto());
	}

	@Test
	@Order(2)
	@DisplayName("Não deve permitir duplicação de usuário")
	public void naoDeveDuplicarUsuario() {
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, "Adriana Mucciolo", "adriana_mucciolo@gmail.com",
				"12345678", "http://fotolegaladriana.jpg"));

		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(new UsuarioModel(0L, "Adriana Mucciolo",
				"adriana_mucciolo@gmail.com", "12345678", "http://fotolegaladriana.jpg"));

		ResponseEntity<UsuarioModel> resposta = testRestTemplate.exchange("/usuarios/register", HttpMethod.POST,
				requisicao, UsuarioModel.class);

		assertEquals(HttpStatus.BAD_REQUEST, resposta.getStatusCode());
	}

	@Test
	@Order(3)
	@DisplayName("Alterar um usuário")
	public void deveAlterarUmUsuario() {
		Optional<UsuarioModel> usuarioCreate = usuarioService
				.cadastrarUsuario(new UsuarioModel(0L, "Joyce", "joyce@gmail.com", "12345678", "http://fotoJoyce.jpg"));

		UsuarioModel usuarioUpdate = new UsuarioModel(usuarioCreate.get().getId(), "Joyce Meireles",
				"joyce_meireles@gmail.com", "12345678", "http://fotoJoyce.jpg");

		HttpEntity<UsuarioModel> requisicao = new HttpEntity<UsuarioModel>(usuarioUpdate);

		ResponseEntity<UsuarioModel> resposta = testRestTemplate.withBasicAuth("root", "root")
				.exchange("/usuarios/update", HttpMethod.PUT, requisicao, UsuarioModel.class);

		assertEquals(HttpStatus.OK, resposta.getStatusCode());
		assertEquals(usuarioUpdate.getNome(), resposta.getBody().getNome());
		assertEquals(usuarioUpdate.getUsuario(), resposta.getBody().getUsuario());
		assertEquals(usuarioUpdate.getFoto(), resposta.getBody().getFoto());
	}

	@Test
	@Order(4)
	@DisplayName("Listar todos os usuários")
	public void deveMostrarTodosOsUsuarios() {
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, "Kevim Lhouis",
				"kevim.lhouis@gmail.com", "12345678", "http://fotoKevim.jpg"));
		usuarioService.cadastrarUsuario(new UsuarioModel(0L, "Vanessa Jesus",
				"vanessa.jesus@gmail.com", "12345678", "http://fotoVanessa.jpg"));
	
		ResponseEntity<String> resposta = testRestTemplate
				.withBasicAuth("root", "root")
				.exchange("/usuarios/all", HttpMethod.GET, null, String.class);
		
		assertEquals(HttpStatus.OK, resposta.getStatusCode());
	}
}
