package artistas;

import java.util.ArrayList;
import java.util.List;

import recitales.Banda;
import recitales.Contrato_x_Cancion;

public class ArtistaInvitado extends Artista {
	private int maxCanciones;
	
	public ArtistaInvitado() {
	}
	
	public ArtistaInvitado(String nombre, List<String> roles, List<Banda> bandas, double costo, int maxCanciones) {
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
	
	public double getCostoBase() {
		return this.tieneDescuento() ? costoBase*0.5 : costoBase ;
	}
	
	
	public boolean tieneDescuento() {
		
   		for (Banda banda : bandas) {
   			System.out.println(banda);
			for (Artista integrante : banda.getIntegrantes()) {
				if(integrante.esBase())
				{
					System.out.println("SI comparte banda" + banda);
					return true;
				}
			}  
		}
   
   		System.out.println("sin descuento");
		return false;
	}
	
	public boolean getDisponibilidad() {
		int cantDisp= maxCanciones-contratos.size();
		return cantDisp!=0;
			
	}
	
	
	
}
