package recitales;

import java.util.ArrayList;
import java.util.List;

public class Cancion {
	private String titulo;
	private boolean estado;
	private List<String> rol = new ArrayList <String>();
	private List<Contrato_x_Cancion> contratos = new ArrayList <Contrato_x_Cancion>();
	
	public Cancion(String titulo, boolean estado, List<String> rol) {
		this.titulo = titulo;
		this.estado = false;
		this.rol = rol;
	}
	
	
	
	
}
