package recitales;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import artistas.ArtistaInvitado;
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
	
	public Cancion getCancion(int i) {
		return canciones.get(i);
	}

	public int cuantosRolesFaltanEnTodas() {
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

	// para ej5
	public String getArtistasFormato() {
		String artistasTexto = "";

		for (int i = 0; i < artistas.size(); i++) {
			artistasTexto += (i + 1) + ") ";
			artistasTexto += artistas.get(i).getNombre();
			artistasTexto += "\n";
		}

		return artistasTexto;
	}

	// REVISAR
/////////////////////////punto 3

	public boolean contratarParaCancion(Cancion cancion) {

		Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();
		List<Artista> artistasBase = this.artistasBaseListaDisp(cancion);
		List<ArtistaInvitado> invitadosDisponibles = this.getArtistasInvDisponibles(cancion);
		Map<Artista, String> yaAsignados = new HashMap<>();

//asignar artistas base primero, ordenando roles por dificultad en bases
		List<String> rolesOrdenadosBase = new ArrayList<>(rolesFaltantes.keySet());
		rolesOrdenadosBase.sort((r1, r2) -> {
			int c1 = cuantosBaseCubren(r1);
			int c2 = cuantosBaseCubren(r2);
			return Integer.compare(c1, c2);
		});

		for (String rol : rolesOrdenadosBase) {
			int faltan = rolesFaltantes.get(rol);

			for (int i = 0; i < artistasBase.size() && faltan > 0; i++) {
				Artista base = artistasBase.get(i);

				if (!yaAsignados.containsKey(base) && base.contieneRol(rol)) {
					yaAsignados.put(base, rol);
					faltan--;
				}
			}

			rolesFaltantes.put(rol, faltan);
		}

// Intentar asignar invitados (sin entrenamiento)
		List<String> roles = new ArrayList<>(rolesFaltantes.keySet());

		roles.sort((r1, r2) -> {
			int c1 = cuantosInvitadosCubren(r1, invitadosDisponibles);
			int c2 = cuantosInvitadosCubren(r2, invitadosDisponibles);
			return Integer.compare(c1, c2);
		});

		for (String rol : roles) {
			int faltan = rolesFaltantes.get(rol);
			if (faltan <= 0)
				continue;

			int asignadosEnEstaRonda = 0;

			for (int i = 0; i < faltan; i++) {

				List<ArtistaInvitado> candidatos = new ArrayList<>();
				for (ArtistaInvitado inv : invitadosDisponibles) {
					if (!yaAsignados.containsKey(inv) && inv.contieneRol(rol)) {
						candidatos.add(inv);
					}
				}

				if (candidatos.isEmpty()) {
					break; // no hay más invitados que puedan cubrir este rol
				}

				candidatos.sort(Comparator.comparingDouble(Artista::getCostoBase));
				ArtistaInvitado elegido = candidatos.get(0);

				yaAsignados.put(elegido, rol);
				asignadosEnEstaRonda++;
			}

			rolesFaltantes.put(rol, faltan - asignadosEnEstaRonda);
		}

// Crear los contratos para los artistas asignados
		for (Map.Entry<Artista, String> e : yaAsignados.entrySet()) {
			Artista art = e.getKey();
			String rol = e.getValue();

			Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, cancion, rol);
			cancion.agregarContrato(contrato);
			art.agregarContrato(contrato);
		}

		return true;
	}

///////////////////////PARA PUNTO 3 : ENTRENAR ////////////////////////////////

	public void entrenarArtistasYContratar(Map<ArtistaInvitado, String> mapa, Cancion cancion) {

		for (Map.Entry<ArtistaInvitado, String> e : mapa.entrySet()) {
			ArtistaInvitado art = e.getKey();
			String rol = e.getValue();

// entrenarlo
			art.entrenar(rol);

// crear contrato
			Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, cancion, rol);
			cancion.agregarContrato(contrato);
			art.agregarContrato(contrato);
		}
	}

	public Map<ArtistaInvitado, String> buscarEntrenables(Cancion cancion) {

		Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();
		Map<ArtistaInvitado, String> recomendados = new HashMap<>();

// invitados disponibles aún no asignados
		List<ArtistaInvitado> invitados = this.getArtistasInvDisponibles(cancion);

// filtrar solo los entrenables
		List<ArtistaInvitado> entrenables = new ArrayList<>();
		for (ArtistaInvitado inv : invitados) {
			if (inv.esEntrenable()) {
				entrenables.add(inv);
			}
		}

// ordenar entrenables por costo (entrena al más barato primero)
		entrenables.sort(Comparator.comparingDouble(Artista::getCostoBase));

// por cada rol faltante, asigno uno entrenable
		for (String rol : rolesFaltantes.keySet()) {

			int faltan = rolesFaltantes.get(rol);

			for (int i = 0; i < faltan && !entrenables.isEmpty(); i++) {

				ArtistaInvitado elegido = entrenables.remove(0);
				recomendados.put(elegido, rol);
			}
		}

		return recomendados;
	}

//////////////////////////////////////////////////Punto 4/////////////////////////////////////////////

/////////////////////////////////metodos pto 4//////////////////////////////////
	private int cuantosArtistasCubrenRolGlobal(String rol, List<Artista> artistas) {
		int c = 0;
		for (Artista a : artistas) {
			if (a.contieneRol(rol))
				c++;
		}
		return c;
	}

	public List<Artista> getArtistasDisponibles() {
		List<Artista> art = new ArrayList<Artista>();
		for (int i = 0; i < this.artistas.size(); i++) {
			if (artistas.get(i).getDisponibilidad()) {
				art.add(artistas.get(i));
			}
		}
		return art;
	}

	public boolean contratarParaRecital() {

// Obtener todas las canciones que aún faltan roles por cubrir
		List<Cancion> incompletas = this.getCancionesIncompletas();
		if (incompletas.isEmpty())
			return true;

// Map total de asignaciones: canción , (artista , rol)
		Map<Cancion, Map<Artista, String>> asignaciones = new HashMap<>();
		for (Cancion c : incompletas)
			asignaciones.put(c, new HashMap<>());

// lista total de artistas ordenada por costo
		List<Artista> todos = this.getArtistasDisponibles();
		todos.sort(Comparator.comparingDouble(Artista::getCostoBase));

//inicializar mapa de cupos temporales
		Map<Artista, Integer> cupos = new HashMap<>();
		for (Artista a : todos) {
			if (a instanceof ArtistaInvitado) {
				ArtistaInvitado inv = (ArtistaInvitado) a;
				int inicial = inv.getMaxCanciones() - inv.getContratos().size(); // max - ya contratadas
				cupos.put(a, Math.max(0, inicial));
			} else {
				cupos.put(a, Integer.MAX_VALUE);
			}
		}

// Para cada canción: intentar asignar (y decrementar cupo)
		for (Cancion cancion : incompletas) {

			Map<Artista, String> yaAsignados = asignaciones.get(cancion);
			Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();

// ordenar roles por dificultad 
			List<String> rolesOrdenados = new ArrayList<>(rolesFaltantes.keySet());
			rolesOrdenados.sort((r1, r2) -> {
				int c1 = cuantosArtistasCubrenRolGlobal(r1, todos);
				int c2 = cuantosArtistasCubrenRolGlobal(r2, todos);
				return Integer.compare(c1, c2);
			});

			for (String rol : rolesOrdenados) {

				int faltan = rolesFaltantes.getOrDefault(rol, 0);
				if (faltan <= 0)
					continue;

				for (int i = 0; i < faltan; i++) {

					Artista elegido = null;

					for (Artista art : todos) {

// candidato: no asignado en ESTA canción, sabe el rol y tiene cupo > 0
						boolean noAsignadoACancion = !yaAsignados.containsKey(art);
						boolean sabeRol = art.contieneRol(rol);
						int cupoActual = cupos.getOrDefault(art, 0);

						if (noAsignadoACancion && sabeRol && cupoActual > 0) {
							elegido = art;
							break;
						}
					}

					if (elegido == null)
						break; // no hay candidato

// asigno y descuento su cupo en el map
					yaAsignados.put(elegido, rol);
					cupos.put(elegido, cupos.get(elegido) - 1);
				}

// actualizar faltantes (conteo de lo asignado para este rol en esta canción)
				int asignadosRol = (int) yaAsignados.values().stream().filter(r -> r.equals(rol)).count();
				rolesFaltantes.put(rol, Math.max(0, faltan - asignadosRol));
			}
		}

// crear todos los contratos reales a partir de las asignaciones 
		for (Cancion c : incompletas) {
			Map<Artista, String> map = asignaciones.get(c);
			for (Map.Entry<Artista, String> e : map.entrySet()) {
				Artista art = e.getKey();
				String rol = e.getValue();

				Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, c, rol);
				c.agregarContrato(contrato);
				art.agregarContrato(contrato);
//  cupo real = max - contratos.size()
			}
		}

		return true;

	}

///////////////////////////// 4 entrenar ////////////////////////////////////////

////////////////////////////////////////////////
////////////////////////////metodos

//	public List<Cancion> getCanciones() {
//		return canciones;
//	}

	public List<Cancion> getCancionesIncompletas() {
		return canciones.stream().filter(c -> c.rolesFaltantes() > 0).toList();
	}

//	public List<Artista> artistasBaseLista() {
//		List<Artista> artistasBase = new ArrayList<>();
//		for (Artista artista : this.artistas) {
//			if (artista.esBase()) {
//				artistasBase.add(artista);
//			}
//		}
//		return artistasBase;
//	}

	public List<Artista> artistasBaseListaDisp(Cancion cancion) {
		List<Artista> artistasBaseDisp = new ArrayList<>();
		for (Artista artista : this.artistas) {
			if (artista.esBase() && !cancion.cancionTieneArtista(artista)) {
				artistasBaseDisp.add(artista);
			}
		}
		return artistasBaseDisp;
	}

	public List<ArtistaInvitado> getArtistasInvDisponibles(Cancion cancion) {
		List<ArtistaInvitado> artistas = new ArrayList<>();

		for (Artista artista : this.artistas) {
			if (!artista.esBase() && artista.getDisponibilidad() && !cancion.cancionTieneArtista(artista)) {
				artistas.add((ArtistaInvitado) artista);
			}
		}
		return artistas;
	}

	public List<ArtistaInvitado> artistasEntrenables(List<ArtistaInvitado> artDisponibles) {
		List<ArtistaInvitado> entrenables = new ArrayList<ArtistaInvitado>();

		for (ArtistaInvitado art : artDisponibles) {
			if (art.contratosvacio()) {
				entrenables.add(art);
			}
		}

		return entrenables;
	}

	public List<ArtistaInvitado> getTodosLosArtistasInvitados() {
		List<ArtistaInvitado> invitados = new ArrayList<ArtistaInvitado>();

		for (Artista art : artistas) {
			if (!art.esBase()) {
				invitados.add((ArtistaInvitado) art);
			}
		}
		return invitados;
	}

	private int cuantosBaseCubren(String rol) {
		int c = 0;
		List<Artista> artistasBase = this.artistasBaseLista();
		for (Artista b : artistasBase) {
			if (b.contieneRol(rol))
				c++;
		}
		return c;
	}

	private int cuantosInvitadosCubren(String rol, List<ArtistaInvitado> lista) {
		int c = 0;
		for (Artista i : lista) {
			if (i.contieneRol(rol))
				c++;
		}
		return c;
	}

	public Map<ArtistaInvitado, String> buscarEntrenablesReci() {

		Map<ArtistaInvitado, String> recomendados = new HashMap<>();
		Map<ArtistaInvitado, Integer> cupos = new HashMap<>();

// Reunir roles faltantes de todo el recital
		Map<String, Integer> rolesFaltantesTotal = new HashMap<>();

		for (Cancion c : canciones) {
			Map<String, Integer> faltantes = c.rolesFaltantesConCantidad();
			for (String rol : faltantes.keySet()) {
				rolesFaltantesTotal.merge(rol, faltantes.get(rol), Integer::sum);
			}
		}

// Obtener entrenables
		List<ArtistaInvitado> entrenables = new ArrayList<>();

		for (ArtistaInvitado inv : this.getTodosLosArtistasInvitados()) {
			if (inv.getDisponibilidad() && inv.esEntrenable()) {
				entrenables.add(inv);
				cupos.put(inv, inv.getCupoDisponible()); // CUPOS INICIALES
			}
		}

		if (entrenables.isEmpty())
			return recomendados;

//Ordenar por costo base
		entrenables.sort(Comparator.comparingDouble(Artista::getCostoBase));

//Asignar entrenables con cupos decreciendo
		for (String rol : rolesFaltantesTotal.keySet()) {

			int faltan = rolesFaltantesTotal.get(rol);

			for (int i = 0; i < faltan && !entrenables.isEmpty(); i++) {

// tomar el más barato disponible
				ArtistaInvitado elegido = entrenables.get(0);

// si no tiene cupo -> eliminarlo de la lista
				if (cupos.get(elegido) == 0) {
					entrenables.remove(0);
					i--;
					continue;
				}

// asignar
				recomendados.put(elegido, rol);

// decrementar cupo
				cupos.put(elegido, cupos.get(elegido) - 1);

// si gastó su último cupo -> eliminarlo
				if (cupos.get(elegido) == 0) {
					entrenables.remove(0);
				}
			}
		}

		return recomendados;
	}

	public void entrenarArtistasYContratarRecital(Map<ArtistaInvitado, String> mapa) {

// mapa -> artista entrenable : rol sugerido 
		Map<ArtistaInvitado, Integer> cupos = new HashMap<>();

// cargar cupos iniciales (solo una vez)
		for (ArtistaInvitado art : mapa.keySet()) {
			cupos.put(art, art.getCupoDisponible());
		}

		for (Map.Entry<ArtistaInvitado, String> e : mapa.entrySet()) {

			ArtistaInvitado art = e.getKey();
			String rol = e.getValue();
// si ya no tiene cupo -> no entrenar ni contratar
			if (cupos.getOrDefault(art, 0) == 0)
				continue;
// ENTRENAR 
			art.entrenar(rol);

//RECORRER TODAS LAS CANCIONES Y ASIGNAR HASTA AGOTAR CUPO
			for (Cancion c : canciones) {

// si ya no tiene cupo, salimos del loop de canciones para este artista
				if (cupos.getOrDefault(art, 0) <= 0) {
					break;
				}

// si la canción necesita el rol y aún queda cupo
				while (c.necesitaRol(rol) && cupos.getOrDefault(art, 0) > 0) {

					boolean yaContratadoEnEstaCancion = false;
					for (Contrato_x_Cancion cc : c.getContratos()) {

						if (cc.getArtista().equals(art)) {
							yaContratadoEnEstaCancion = true;
							break;
						}
					}

					if (yaContratadoEnEstaCancion) {
// no se puede asignar dos contratos del mismo artista en la misma canción
						break; // pasar a la siguiente canción
					}

// crear contrato
					Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, c, rol);
					c.agregarContrato(contrato);
					art.agregarContrato(contrato);

// disminuir cupo en el mapa (cupo dinámico)
					cupos.put(art, cupos.get(art) - 1);
				}
// seguir a la siguiente canción y volver a intentar mientras quede cupo
			}
		}
	}

}
