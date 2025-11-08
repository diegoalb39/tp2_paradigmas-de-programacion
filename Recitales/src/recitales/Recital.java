package recitales;

import java.util.ArrayList;
import java.util.List;

import artistas.Artista;

public class Recital {
	private String nombre;
	private List<Cancion> canciones = new ArrayList <Cancion>();
	private List<Artista> artistas = new ArrayList <Artista>();
	
	
	public Recital(String nombre, List<Cancion> canciones) {
		this.nombre = nombre;
		this.canciones = canciones;
	}
	
	
	public int verRolesFaltantesCancion(int indice) {
		return canciones.get(indice).rolesFaltantes();
	}
	
	public String getCanciones() {
		String titulos="";
		
		for (int i=0; i< canciones.size() ; i++) {
		titulos+=(i+1) + ") ";
		titulos+=canciones.get(i).getTitulo();
		titulos+="\n";
		}
		
		return titulos;
	}
	
	public int rolesFaltantesEnTodas() {
		int cant=0;
		for (Cancion cancion : canciones) {
			cant+=cancion.rolesFaltantes();
		}
		return cant;
	
	}
	
	
	
}
