package prolog;

import java.util.List;

public class CancionData {
	private String nombre;
	private List<String> rolesRequeridos; 

	public CancionData(String nombre, List<String> rolesRequeridos) {
		this.nombre = nombre;
		this.rolesRequeridos = rolesRequeridos;
	}

	
	public String getNombre() {
		return nombre;
	}

	public List<String> getRolesRequeridos() {
		return rolesRequeridos;
	}
}

