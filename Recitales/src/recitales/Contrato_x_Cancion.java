package recitales;

import artistas.Artista;

public class Contrato_x_Cancion {
	private Artista artista;
	private Cancion cancion;
	private double costo;
	private String rol;
	
	
	public Contrato_x_Cancion(Artista artista, Cancion cancion, String rol) {
		this.artista = artista;
		this.cancion = cancion;
		this.costo =artista.getCosto();
		this.rol=rol;
	}


	public Artista getArtista() {
		return artista;
	}

	public Cancion getCancion() {
		return cancion;
	}


	public double getCosto() {
		return costo;
	}



	public String getRol() {
		return rol;
	}


	@Override
	public String toString() {
		return "Contrato_x_Cancion [artista=" + artista + ", cancion=" + cancion + ", costo=" + costo + ", rol=" + rol
				+ "]";
	}


	
}
