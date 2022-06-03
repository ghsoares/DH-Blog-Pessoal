package com.blogpessoal.blogpessoal.service;

import java.nio.charset.Charset;
import java.util.Optional;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.blogpessoal.blogpessoal.model.UsuarioModel;
import com.blogpessoal.blogpessoal.model.UsuarioLogin;
import com.blogpessoal.blogpessoal.repository.UsuarioRepository;

@Service
public class UsuarioService {

	@Autowired
	private UsuarioRepository repository;

	public Optional<UsuarioModel> cadastrarUsuario(UsuarioModel usuario) {
		if (repository.findByUsuario(usuario.getUsuario()).isPresent())
			throw new ResponseStatusException(
					HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
		
		usuario.setSenha(encryptPassword(usuario.getSenha()));
		
		return Optional.of(repository.save(usuario));
	}
	
	public Optional<UsuarioModel> atualizarUsuario(UsuarioModel usuario) {
		if (repository.findById(usuario.getId()).isPresent()) {
			Optional<UsuarioModel> user = repository.findByUsuario(usuario.getUsuario());
			
			if (user.isPresent() && user.get().getId() != usuario.getId()) {
				throw new ResponseStatusException(
						HttpStatus.BAD_REQUEST, "Usuário já existe!", null);
			}
			
			usuario.setSenha(encryptPassword(usuario.getSenha()));
			
			return Optional.ofNullable(repository.save(usuario));
		}
		
		return Optional.empty();
	}

	public Optional<UsuarioLogin> logar(Optional<UsuarioLogin> user) {
		Optional<UsuarioModel> usuario = repository.findByUsuario(user.get().getUsuario());

		if (usuario.isPresent()) {
			if (passwordMatches(user.get().getSenha(), usuario.get().getSenha())) {
				
				user.get().setId(usuario.get().getId());
				user.get().setNome(usuario.get().getNome());
				user.get().setFoto(usuario.get().getFoto());
				user.get().setToken(generateBasicToken(user.get().getUsuario(), user.get().getSenha()));
				user.get().setSenha(usuario.get().getSenha());
				
				return user;
			}
		}

		return Optional.empty();
	}
	
	private String encryptPassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.encode(password);
	}
	
	private boolean passwordMatches(String p1, String p2) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		
		return encoder.matches(p1, p2);
	}
	
	private String generateBasicToken(String username, String password) {
		String token = username + ":" + password;
		
		byte[] tokenBase64 = Base64.encodeBase64(token.getBytes(Charset.forName("US-ASCII")));
		
		return "Basic " + new String(tokenBase64);
	}
}
