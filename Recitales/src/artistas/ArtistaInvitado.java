package artistas;

import java.util.List;
import recitales.Banda;

public class ArtistaInvitado extends Artista {
	private int maxCanciones;

	public ArtistaInvitado() {
	}

	public ArtistaInvitado(String nombre, List<String> roles, List<Banda> bandas, double costo, int maxCanciones) {
		super(nombre, roles, bandas);
		this.costoBase = costo;
		this.maxCanciones = maxCanciones;
	}

	public boolean esBase() {
		return false;
	}

	public boolean entrenar(String rol) {

		if (this.contratos.isEmpty() && !this.esBase() && !this.roles.contains(rol)) {
			this.roles.add(rol);
			this.costoBase *= 1.5;
			return true;
		}

		return false;
	}

	public double getCostoBase() {
		return this.tieneDescuento() ? costoBase * 0.5 : costoBase;
	}

	public boolean tieneDescuento() {

		for (Banda banda : bandas) {
			for (Artista integrante : banda.getIntegrantes()) {
				if (integrante.esBase()) {
					return true;
				}
			}
		}

		return false;
	}

	public boolean getDisponibilidad() {
		int cantDisp = maxCanciones - contratos.size();
		return cantDisp != 0;

	}
	
	public boolean esEntrenable() {
		return this.contratosvacio();
	}

	public int getMaxCanciones() {
		return this.maxCanciones;
	}
	
	public int getCupoDisponible() {
		return  maxCanciones-contratos.size();
	}
	
	@Override
	public String toString() {
		return "ArtistaInvitado [nombre " + this.nombre + "  maxCanciones=" + maxCanciones + "]";
	}
}
