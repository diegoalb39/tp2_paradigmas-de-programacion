package recitales;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import artistas.Artista;

public class Cancion {
	private String titulo;
	private List<String> roles;
	private List<Contrato_x_Cancion> contratos = new ArrayList <Contrato_x_Cancion>();
	
	public Cancion(String titulo, List<String> rol) {
		this.titulo = titulo;
		this.roles = rol;
	}
	
	public Cancion() {
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
		return this.roles.contains(rol);
	}
	
	public boolean cancionTieneArtista(Artista artista) {
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
		
		for (String rold : this.roles) {
			if(rold.equals(rol)) {
				cantRol++;
			}
		}
			return cantContr<cantRol;
	}
	
	public int rolesFaltantes() {
			return roles.size()-contratos.size();
	}
	
//	¿Qué roles (con cantidad) me faltan para tocar una canción X del recital?
	public Map<String,Integer> rolesFaltantesConCantidad(){
		Map<String,Integer> rolesconcantidad= new HashMap<String, Integer>();
		int cantidadRol=0, cantidadContrato=0;
		
		for (String rold : roles) {
			if(!rolesconcantidad.containsKey(rold))
			{	
				cantidadRol=Collections.frequency(roles, rold);
				
				cantidadContrato=0;
				for (Contrato_x_Cancion contrato : contratos) {
					if(contrato.getRol().equals(rold)) {
						cantidadContrato++;
					}
				}				
				rolesconcantidad.put(rold,cantidadRol - cantidadContrato);
			}
		}
				
		return rolesconcantidad;
	}
	
	
	public String getTitulo() {
		return titulo;
	}

	public List<String> getRoles() {
		return roles;
	}

	public List<Contrato_x_Cancion> getContratos() {
		return contratos;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}

	public void setContratos(List<Contrato_x_Cancion> contratos) {
		this.contratos = contratos;
	}

	@Override
	public String toString() {
		//return "Cancion [titulo=" + titulo + ", roles=" + roles + ", contratos=" + contratos + "]";
		return "Cancion [titulo=" + titulo + ", roles=" + roles + "]";
	}
	
	//PUNTO 7
	public double calcularCosto() {
		double total = 0;
		for (Contrato_x_Cancion c : contratos) {
			total += c.getCosto();
		}
		return total;
	}
	// true si no falta ningún rol
	public boolean tieneTodosLosRolesCubiertos() {
		Map<String, Integer> faltantes = this.rolesFaltantesConCantidad();
		for (Integer cant : faltantes.values()) {
			if(cant > 0) {
				return false;// si falta al menos 1 de algún rol -> INCOMPLETA
			}
		}
		return true;
	}
	
	//2-Bonus
	public void quitarContratosDe(Artista artista) {
	// recorro con iterador para poder remover el elemento mientras lo recorro, con un for seria mas dificil
		Iterator<Contrato_x_Cancion> it = contratos.iterator();
		while(it.hasNext()) {
			Contrato_x_Cancion c = it.next();
			if(c.getArtista().equals(artista)) {
				it.remove();
			}
		}
	}
	
	//REVISAR
	public void agregarContrato(Contrato_x_Cancion contrato) {
		this.contratos.add(contrato);
	}
	
	public void mostrarContratos() {
		for (Contrato_x_Cancion contrato : contratos) {
			System.out.println(contrato);
		}
	}
	
	public boolean necesitaRol(String rol) {
	    int cantidadNecesaria = this.rolesFaltantesConCantidad().getOrDefault(rol, 0);
	    return cantidadNecesaria > 0;
	}
}
