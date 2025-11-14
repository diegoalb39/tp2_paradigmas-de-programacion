package io;

import java.util.List;

public class CancionJson {
	private String titulo;
	private List<String> rolesRequeridos;

	public CancionJson() {
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public List<String> getRolesRequeridos() {
		return rolesRequeridos;
	}

	public void setRolesRequeridos(List<String> rolesRequeridos) {
		this.rolesRequeridos = rolesRequeridos;
	}

	@Override
	public String toString() {
		return "Cancion{" + titulo + ", rolesRequeridos=" + rolesRequeridos + "}";
	}
}
