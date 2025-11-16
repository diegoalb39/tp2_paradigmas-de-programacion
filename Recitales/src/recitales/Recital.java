package recitales;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import artistas.Artista;

public class Recital {
	private String nombre;
	private List<Cancion> canciones;
	private List<Artista> artistas;
	
	
	public Recital(String nombre, List<Cancion> canciones, List<Artista> artistas) {
		this.nombre = nombre;
		this.canciones = canciones;
		this.artistas= artistas;
	}
	
	
	
	public List<Artista> getArtistas() {
		return artistas;
	}

	//	¿Qué roles (con cantidad) me faltan para tocar una canción X del recital?
	public int cuantosRolesFaltanCancion(int indice) {
		return canciones.get(indice).rolesFaltantes();
	}
	
	public Map<String,Integer> rolesFaltantesCancionCantidad(int indice){
		return canciones.get(indice).rolesFaltantesConCantidad();
	}
	
	
	public String getTituloCanciones() {
		String titulos="";
		
		for (int i=0; i< canciones.size() ; i++) {
		titulos+=(i+1) + ") ";
		titulos+=canciones.get(i).getTitulo();
		titulos+="\n";
		}
		
		return titulos;
	}
	
	public List<Cancion> getCanciones() {
		return canciones;
	}
	
	
	public int rolesFaltantesEnTodas() {
		int cant=0;
		for (Cancion cancion : canciones) {
			cant+=cancion.rolesFaltantes();
		}
		return cant;
	
	}
	
	
	

	
	
	
}
