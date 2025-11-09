package artistas;

import java.util.ArrayList;
import java.util.List;

import recitales.Banda;
import recitales.Contrato_x_Cancion;

public class Artista {
	private String nombre;
	protected double costoBase;
	protected List<String>roles;
	protected List<Banda>bandas;
	protected List<Contrato_x_Cancion>contratos = new ArrayList<Contrato_x_Cancion>();
	
	public Artista(String nombre, List<String> roles, List<Banda> bandas) {
		this.nombre=nombre;
		costoBase=0;
		this.roles=roles;
		this.bandas=bandas;

	}
	
	public double getCosto() {
		return this.costoBase;
	}
	
	public boolean esBase() {
		return true;
	}
	
	
	public boolean getDisponibilidad() {
			return true;
		
	}
	
	public boolean contieneRol(String rol) {
		if(this.roles.contains(rol))
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
	
	

	
}
