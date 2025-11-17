package recitales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import artistas.Artista;

public class Recital {
	private String nombre;
	private List<Cancion> canciones;
	private List<Artista> artistas;

	public Recital(String nombre, List<Cancion> canciones, List<Artista> artistas) {
		this.nombre = nombre;
		this.canciones = canciones;
		this.artistas = artistas;
	}

	public String getNombre() {
		return nombre;
	}

	public List<Artista> getArtistas() {
		return artistas;
	}

	// ¿Qué roles (con cantidad) me faltan para tocar una canción X del recital?
	public int cuantosRolesFaltanCancion(int indice) {
		return canciones.get(indice).rolesFaltantes();
	}

	public Map<String, Integer> rolesFaltantesCancionCantidad(int indice) {
		return canciones.get(indice).rolesFaltantesConCantidad();
	}

	public String getTituloCanciones() {
		String titulos = "";

		for (int i = 0; i < canciones.size(); i++) {
			titulos += (i + 1) + ") ";
			titulos += canciones.get(i).getTitulo();
			titulos += "\n";
		}

		return titulos;
	}

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public int rolesFaltantesEnTodas() {
		int cant = 0;
		for (Cancion cancion : canciones) {
			cant += cancion.rolesFaltantes();
		}
		return cant;

	}

	// 2-Bonus
	public Artista busacarArtistaPorNombre(String nombre) {
		for (Artista a : artistas) {
			if (a.getNombre().equalsIgnoreCase(nombre)) {
				return a;
			}
		}
		return null;
	}

	public void quitarArtistaDelRecital(Artista artista) {
		if (artista == null) {
			return;
		}
		// 1) quitar todos sus contratos de todas las canciones
		for (Cancion c : canciones) {
			c.quitarContratosDe(artista);
		}
		// 2)limpiar contratos del lado del artista
		artista.getContratos().clear();
		// 3)sacarlo de la lista de artistas participantes ->no se si dejarlo, ya que
		// sigue existiendo, solo que no participa
		artistas.remove(artista);// creo que se saca
	}

	/// Punto 2 Roles con cantidad que faltan para tocar todas las canciones
	public List<Artista> artistasBaseLista() {
		List<Artista> artistasBase = new ArrayList<>();
		for (Artista artista : this.artistas) {
			if (artista.esBase()) {
				artistasBase.add(artista);
			}
		}
		return artistasBase;
	}

	public Map<String, Integer> rolesFaltantesTodasCanciones() {

		Map<String, Integer> faltantesTotales = new HashMap<>();
		List<Artista> artistasBase = this.artistasBaseLista();
		Map<String, Integer> rolesRequeridos;
		Map<String, List<Artista>> consideraciones = new HashMap<>(); // lista de artistas base que pueden cubrir el rol
		Set<Artista> yaAsignados = new HashSet<>();

		for (Cancion cancion : canciones) { // Para cada cancion

			consideraciones.clear();
			yaAsignados.clear();

			// Obtener los roles requeridos
			rolesRequeridos = cancion.rolesFaltantesConCantidad();

			for (String rol : rolesRequeridos.keySet()) {
				List<Artista> disponibles = new ArrayList<>();

				for (Artista a : artistasBase) {
					if (a.contieneRol(rol)) {
						disponibles.add(a);
					}
				}

				consideraciones.put(rol, disponibles);
			}

			// Ordenar roles por cantidad de artistas disponibles de menor a mayor
			List<String> rolesOrdenados = consideraciones.keySet().stream()
					.sorted(Comparator.comparingInt(r -> consideraciones.get(r).size())).toList();

			// Asignar artistas a los roles en ese orden
			for (String rol : rolesOrdenados) { // itera sobre los roles

				int cantidadNecesaria = rolesRequeridos.get(rol);
				int asignados = 0;

				// itera sobre la lista de art disponibles
				for (Artista a : consideraciones.get(rol)) {

					if (!yaAsignados.contains(a)) { // si el artista no fue asignado a la cancion previamente
						yaAsignados.add(a); // lo asigna
						asignados++;
					}
					// Si complete los artistas necesarios para cubrir el rol, salgo
					if (asignados == cantidadNecesaria)
						break;
				}

				// cuantos quedaron faltantes en esta canción
				int faltan = cantidadNecesaria - asignados;

				if (faltan > 0) {
					// merge, suma "faltan" al value del rol, si el rol no existe lo crea con ese
					// valor.
					faltantesTotales.merge(rol, faltan, Integer::sum);
				}
			}
		}

		return faltantesTotales;
	}

	//////////////////////////////////////////////

	// oara ej5
	public String getArtistasFormato() {
		String artistasTexto = "";

		for (int i = 0; i < artistas.size(); i++) {
			artistasTexto += (i + 1) + ") ";
			artistasTexto += artistas.get(i).getNombre();
			artistasTexto += "\n";
		}

		return artistasTexto;
	}

}
