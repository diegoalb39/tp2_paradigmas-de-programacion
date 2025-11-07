package recitales;

import java.util.ArrayList;
import java.util.List;

public class Banda {
	private String nombre;
	private List<Artista> integrantes;
	
	public Banda(String nombre, List<Artista> integrantes) {
		super();
		this.nombre = nombre;
		this.integrantes = integrantes;
	}

	public String getNombre() {
		return nombre;
	}

	public List<Artista> getIntegrantes() {
		return integrantes;
	}

	
	
	
	
}
