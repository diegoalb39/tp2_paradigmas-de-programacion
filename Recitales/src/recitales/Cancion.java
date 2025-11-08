package recitales;

import java.util.ArrayList;
import java.util.List;

import artistas.Artista;

public class Cancion {
	private String titulo;
	private List<String> rol;
	private List<Contrato_x_Cancion> contratos = new ArrayList <Contrato_x_Cancion>();
	
	public Cancion(String titulo, List<String> rol) {
		this.titulo = titulo;
		this.rol = rol;
	}
	
	public boolean contratarArtista(Artista artista, String rol) {
		if( !this.cancionContieneRol(rol) || !artista.getDisponibilidad() || !artista.contieneRol(rol) || this.cancionTieneArtista(artista) || !this.rolEstaDisponible(rol) ) {
			return false;
		}
		Contrato_x_Cancion contratoNuevo = new Contrato_x_Cancion(artista,this,rol);
		this.contratos.add(contratoNuevo);	
		artista.agregarContrato(contratoNuevo);
			return true;		
	}
	
	private boolean cancionContieneRol(String rol) {
		return this.rol.contains(rol);
	}
	
	private boolean cancionTieneArtista(Artista artista) {
		for (Contrato_x_Cancion contrato : this.contratos) {
	        if (contrato.getArtista().equals(artista)) {
	            return true;
	        }
	    }
	    return false;
	}
	
	private boolean rolEstaDisponible(String rol) {
		int cantContr=0, cantRol=0;
		for (Contrato_x_Cancion contrato : this.contratos) {
	        if (contrato.getRol().equals(rol)) {
	        	cantContr++;
	        }
	    }
		
		for (String rold : this.rol) {
			if(rold.equals(rol)) {
				cantRol++;
			}
		}

			return cantContr<cantRol;
	}
	
	public int rolesFaltantes() {
			
			return rol.size()-contratos.size();
	}
	
	
	public String getTitulo() {
		return titulo;
	}
	
	
	
	
}
