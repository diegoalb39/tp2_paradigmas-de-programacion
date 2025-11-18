package artistas;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import recitales.Banda;
import recitales.Contrato_x_Cancion;

public class Artista {
	protected String nombre;
	protected double costoBase;
	protected List<String> roles;
	protected List<Banda> bandas;
	protected List<Contrato_x_Cancion> contratos = new ArrayList<Contrato_x_Cancion>();

	public Artista() {// jackson necesita contructores vacios
	}

	public Artista(String nombre, List<String> roles, List<Banda> bandas) {
		this.nombre = nombre;
		costoBase = 0;
		this.roles = roles;
		this.bandas = bandas;

	}

	public String getNombre() {
		return nombre;
	}

	public double getCostoBase() {
		return this.costoBase;
	}

	public List<String> getRoles() {
		return roles;
	}

	public List<Banda> getBandas() {
		return bandas;
	}

	public List<Contrato_x_Cancion> getContratos() {
		return contratos;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public void setCostoBase(double costoBase) {
		this.costoBase = costoBase;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setBandas(List<Banda> bandas) {
		this.bandas = bandas;
	}

	public void setContratos(List<Contrato_x_Cancion> contratos) {
		this.contratos = contratos;
	}

	public boolean esBase() {
		return true;
	}

	public boolean getDisponibilidad() {
		return true;

	}

	public boolean contieneRol(String rol) {
		if (this.roles.contains(rol))
			return true;
		else
			return false;
	}

	public void agregarContrato(Contrato_x_Cancion contrato) {
		this.contratos.add(contrato);
	}

	@Override
	public String toString() {
		return "Artista [nombre=" + nombre + ", costoBase=" + costoBase + "]";
	}

	// PUNTO 6
	public boolean tieneContratos() {
		if (this.getContratos().size() > 0) {
			return true;
		}
		return false;
	}
	
	//REVISAR
	public boolean contratosvacio() {
		return this.contratos.isEmpty();
	}

	public int getMaxCanciones() {
		return 100;
	}
	public int getCupoDisponible() {
		return 100;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nombre);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artista other = (Artista) obj;
		return Objects.equals(nombre, other.nombre);
	}
	

}
