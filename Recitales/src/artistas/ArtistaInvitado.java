package artistas;

import java.util.ArrayList;
import java.util.List;

import recitales.Banda;

public class ArtistaInvitado extends Artista {
	private int maxCanciones;
	
	public ArtistaInvitado(String nombre, double costo, int maxCanciones, List<String> roles, List<Banda> bandas) {
		super(nombre, roles, bandas);
		this.costoBase= costo;
		this.maxCanciones=maxCanciones;
	}
	
	public boolean esBase() {
		return false;
	}
	
	public boolean entrenar(String rol) {

		if(this.contratos.isEmpty() && !this.esBase()) {
			this.roles.add(rol);
			this.costoBase*=1.5;
			return true;	
		}
		
		return false;
	}
	
	public double getCosto() {
		
		return this.tieneDescuento() ? costoBase*0.5 : costoBase ;
	}
	
	
	public boolean tieneDescuento() {
		
   		for (Banda banda : bandas) {
			for (Artista integrante : banda.getIntegrantes()) {
				if(integrante.esBase())
				{
					return true;
				}
			}  
		}
		return false;
	}
	
	public boolean getDisponibilidad() {
		int cantDisp= maxCanciones-contratos.size();
		if(cantDisp==0)
			return true;
		else
			return false;
	}
	
	
}
