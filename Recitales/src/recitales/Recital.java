package recitales;

import java.util.ArrayList;
import java.util.List;

public class Recital {
	private String nombre;
	private List<Cancion> canciones = new ArrayList <Cancion>();
	private List<Artista> artistas = new ArrayList <Artista>();
	
	
	public Recital(String nombre) {
		this.nombre = nombre;
	}
	
	
	public int verRolesFaltantesCancion() {
		System.out.println("Los roles son");
		return 1;
	}
	
	
}
