package io;

import java.util.List;

public class ArtistaJson {
	private String nombre;
	private List<String> roles;
	private List<String> bandas;
	private double costo;
	private int maxCanciones;
	
	public ArtistaJson(){
	}

	public String getNombre() {
		return nombre;
	}

	public List<String> getRoles() {
		return roles;
	}

	public List<String> getBandas() {
		return bandas;
	}

	public double getCosto() {
		return costo;
	}

	public int getMaxCanciones() {
		return maxCanciones;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setBandas(List<String> bandas) {
		this.bandas = bandas;
	}

	public void setCosto(double costo) {
		this.costo = costo;
	}

	public void setMaxCanciones(int maxCanciones) {
		this.maxCanciones = maxCanciones;
	}

	@Override
	public String toString() {
		return "ArtistaJson [nombre=" + nombre + ", roles=" + roles + ", bandas=" + bandas + ", costo=" + costo
				+ ", maxCanciones=" + maxCanciones + "]";
	}

	
}
