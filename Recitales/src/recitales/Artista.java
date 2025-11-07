package recitales;

import java.util.ArrayList;
import java.util.List;

public class Artista {
	private String nombre;
	protected double costoBase;
	protected List<String>roles;
	private List<Banda>bandas;
	protected List<Contrato_x_Cancion>contratos;
	
	public Artista(String nombre, List<String> roles, List<Banda> bandas) {
		this.nombre=nombre;
		costoBase=0;
		this.roles=roles;
		this.bandas=bandas;

	}
	
	public double getosto() {
		return this.costoBase;
	}
	
	public boolean esBase() {
		return true;
	}
	
	
	public boolean getDisponibilidad() {
			return true;
		
	}
	
	
	
}
