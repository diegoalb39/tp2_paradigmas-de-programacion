package io;

import java.util.ArrayList;
import java.util.List;
import recitales.Cancion;

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
	
	public Cancion toCancion() {
		Cancion cancion = new Cancion();
		cancion.setTitulo(this.getTitulo());
		cancion.setRoles(this.getRolesRequeridos());
		
		return cancion;
	}
// Transforma la lista CancionJson importada a una lista de Cancion, no incluye contratos 
	public static List<Cancion> convertirLista(List<CancionJson> listaJson){
		List<Cancion> listaCanciones = new ArrayList<>();
		for (CancionJson l : listaJson) {
			listaCanciones.add(l.toCancion());
		}
		return listaCanciones;
	}
		
}

