package prolog;

import java.util.List;

public class ArtistaData {
	private String nombre;
	private String tipo; // "base" / "no_base"
	private List<String> roles;
	private double costo;

	public ArtistaData(String nombre, String tipo, List<String> roles, double costo) {
		this.nombre = nombre;
		this.tipo = tipo;
		this.roles = roles;
		this.costo = costo;
	}

	public String getNombre() {
		return nombre;
	}

	public String getTipo() {
		return tipo;
	}

	public List<String> getRoles() {
		return roles;
	}

	public double getCosto() {
		return costo;
	}
}