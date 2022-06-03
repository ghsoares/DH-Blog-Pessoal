package com.blogpessoal.blogpessoal.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name="tb_postagem")
public class PostagemModel {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@NotNull
	@Size(min=5, max=100)
	private String titulo;
	
	@NotNull
	@Size(min=16, max=512)
	private String texto;
	
	@UpdateTimestamp
	private LocalDateTime date;
	
	@ManyToOne
	@JsonIgnoreProperties("tb_postagem")
	private TemaModel tema;

	@ManyToOne
	@JsonIgnoreProperties("tb_postagem")
	private UsuarioModel usuario;

	public long getId() {
		return id;
	}

	public String getTitulo() {
		return titulo;
	}

	public String getTexto() {
		return texto;
	}

	public LocalDateTime getDate() {
		return date;
	}

	public TemaModel getTema() {
		return tema;
	}

	public UsuarioModel getUsuario() {
		return usuario;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public void setTema(TemaModel tema) {
		this.tema = tema;
	}

	public void setUsuario(UsuarioModel usuario) {
		this.usuario = usuario;
	}
}
