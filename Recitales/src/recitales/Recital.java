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
import prolog.ArtistaData;
import prolog.CancionData;

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

	public List<Cancion> getCanciones() {
		return canciones;
	}

	public Cancion getCancion(int i) {
		return canciones.get(i);
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

	public int cuantosRolesFaltanCancion(int indice) {
		return canciones.get(indice).rolesFaltantes();
	}

	public Map<String, Integer> rolesFaltantesCancionCantidad(int indice) {
		return canciones.get(indice).rolesFaltantesConCantidad();
	}

	public int cuantosRolesFaltanEnTodas() {
		int cant = 0;
		for (Cancion cancion : canciones) {
			cant += cancion.rolesFaltantes();
		}
		return cant;
	}

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
		for (Cancion c : canciones) {
			c.quitarContratosDe(artista);
		}
		artista.getContratos().clear();
	}

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
		Map<String, List<Artista>> consideraciones = new HashMap<>();
		Set<Artista> yaAsignados = new HashSet<>();

		for (Cancion cancion : canciones) {
			consideraciones.clear();
			yaAsignados.clear();
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
			List<String> rolesOrdenados = consideraciones.keySet().stream()
					.sorted(Comparator.comparingInt(r -> consideraciones.get(r).size())).toList();
			for (String rol : rolesOrdenados) {
				int cantidadNecesaria = rolesRequeridos.get(rol);
				int asignados = 0;
				for (Artista a : consideraciones.get(rol)) {
					if (!yaAsignados.contains(a)) {
						yaAsignados.add(a);
						asignados++;
					}
					if (asignados == cantidadNecesaria)
						break;
				}
				int faltan = cantidadNecesaria - asignados;
				if (faltan > 0) {
					faltantesTotales.merge(rol, faltan, Integer::sum);
				}
			}
		}
		return faltantesTotales;
	}

	public String getArtistasFormato() {
		String artistasTexto = "";

		for (int i = 0; i < artistas.size(); i++) {
			artistasTexto += (i + 1) + ") ";
			artistasTexto += artistas.get(i).getNombre();
			artistasTexto += "\n";
		}

		return artistasTexto;
	}

	public boolean contratarParaCancion(Cancion cancion) {
		Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();
		List<Artista> artistasBase = this.artistasBaseListaDisp(cancion);
		List<ArtistaInvitado> invitadosDisponibles = this.getArtistasInvDisponibles(cancion);
		Map<Artista, String> yaAsignados = new HashMap<>();
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
					break;
				}

				candidatos.sort(Comparator.comparingDouble(Artista::getCostoBase));
				ArtistaInvitado elegido = candidatos.get(0);

				yaAsignados.put(elegido, rol);
				asignadosEnEstaRonda++;
			}
			rolesFaltantes.put(rol, faltan - asignadosEnEstaRonda);
		}
		for (Map.Entry<Artista, String> e : yaAsignados.entrySet()) {
			Artista art = e.getKey();
			String rol = e.getValue();

			Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, cancion, rol);
			cancion.agregarContrato(contrato);
			art.agregarContrato(contrato);
		}
		return true;
	}

	public void entrenarArtistasYContratar(Map<ArtistaInvitado, String> mapa, Cancion cancion) {
		for (Map.Entry<ArtistaInvitado, String> e : mapa.entrySet()) {
			ArtistaInvitado art = e.getKey();
			String rol = e.getValue();
			art.entrenar(rol);

			Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, cancion, rol);
			cancion.agregarContrato(contrato);
			art.agregarContrato(contrato);
		}
	}

	public Map<ArtistaInvitado, String> buscarEntrenables(Cancion cancion) {

		Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();
		Map<ArtistaInvitado, String> recomendados = new HashMap<>();
		List<ArtistaInvitado> invitados = this.getArtistasInvDisponibles(cancion);
		List<ArtistaInvitado> entrenables = new ArrayList<>();
		for (ArtistaInvitado inv : invitados) {
			if (inv.esEntrenable()) {
				entrenables.add(inv);
			}
		}
		entrenables.sort(Comparator.comparingDouble(Artista::getCostoBase));
		for (String rol : rolesFaltantes.keySet()) {

			int faltan = rolesFaltantes.get(rol);
			for (int i = 0; i < faltan && !entrenables.isEmpty(); i++) {

				ArtistaInvitado elegido = entrenables.remove(0);
				recomendados.put(elegido, rol);
			}
		}
		return recomendados;
	}

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
		List<Cancion> incompletas = this.getCancionesIncompletas();
		if (incompletas.isEmpty())
			return true;
		Map<Cancion, Map<Artista, String>> asignaciones = new HashMap<>();
		for (Cancion c : incompletas)
			asignaciones.put(c, new HashMap<>());
		List<Artista> todos = this.getArtistasDisponibles();
		todos.sort(Comparator.comparingDouble(Artista::getCostoBase));
		Map<Artista, Integer> cupos = new HashMap<>();
		for (Artista a : todos) {
			if (a instanceof ArtistaInvitado) {
				ArtistaInvitado inv = (ArtistaInvitado) a;
				int inicial = inv.getMaxCanciones() - inv.getContratos().size();
				cupos.put(a, Math.max(0, inicial));
			} else {
				cupos.put(a, Integer.MAX_VALUE);
			}
		}
		for (Cancion cancion : incompletas) {

			Map<Artista, String> yaAsignados = asignaciones.get(cancion);
			Map<String, Integer> rolesFaltantes = cancion.rolesFaltantesConCantidad();
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
						boolean noAsignadoACancion = !yaAsignados.containsKey(art);
						boolean sabeRol = art.contieneRol(rol);
						int cupoActual = cupos.getOrDefault(art, 0);

						if (noAsignadoACancion && sabeRol && cupoActual > 0) {
							elegido = art;
							break;
						}
					}
					if (elegido == null)
						break;
					yaAsignados.put(elegido, rol);
					cupos.put(elegido, cupos.get(elegido) - 1);
				}
				int asignadosRol = (int) yaAsignados.values().stream().filter(r -> r.equals(rol)).count();
				rolesFaltantes.put(rol, Math.max(0, faltan - asignadosRol));
			}
		}
		for (Cancion c : incompletas) {
			Map<Artista, String> map = asignaciones.get(c);
			for (Map.Entry<Artista, String> e : map.entrySet()) {
				Artista art = e.getKey();
				String rol = e.getValue();

				Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, c, rol);
				c.agregarContrato(contrato);
				art.agregarContrato(contrato);
			}
		}
		return true;
	}

	public List<Cancion> getCancionesIncompletas() {
		return canciones.stream().filter(c -> c.rolesFaltantes() > 0).toList();
	}

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
		Map<String, Integer> rolesFaltantesTotal = new HashMap<>();

		for (Cancion c : canciones) {
			Map<String, Integer> faltantes = c.rolesFaltantesConCantidad();
			for (String rol : faltantes.keySet()) {
				rolesFaltantesTotal.merge(rol, faltantes.get(rol), Integer::sum);
			}
		}
		List<ArtistaInvitado> entrenables = new ArrayList<>();

		for (ArtistaInvitado inv : this.getTodosLosArtistasInvitados()) {
			if (inv.getDisponibilidad() && inv.esEntrenable()) {
				entrenables.add(inv);
				cupos.put(inv, inv.getCupoDisponible());
			}
		}

		if (entrenables.isEmpty())
			return recomendados;
		entrenables.sort(Comparator.comparingDouble(Artista::getCostoBase));
		for (String rol : rolesFaltantesTotal.keySet()) {

			int faltan = rolesFaltantesTotal.get(rol);

			for (int i = 0; i < faltan && !entrenables.isEmpty(); i++) {
				ArtistaInvitado elegido = entrenables.get(0);
				if (cupos.get(elegido) == 0) {
					entrenables.remove(0);
					i--;
					continue;
				}
				recomendados.put(elegido, rol);
				cupos.put(elegido, cupos.get(elegido) - 1);
				if (cupos.get(elegido) == 0) {
					entrenables.remove(0);
				}
			}
		}
		return recomendados;
	}

	public void entrenarArtistasYContratarRecital(Map<ArtistaInvitado, String> mapa) {
		Map<ArtistaInvitado, Integer> cupos = new HashMap<>();
		for (ArtistaInvitado art : mapa.keySet()) {
			cupos.put(art, art.getCupoDisponible());
		}
		for (Map.Entry<ArtistaInvitado, String> e : mapa.entrySet()) {
			ArtistaInvitado art = e.getKey();
			String rol = e.getValue();
			if (cupos.getOrDefault(art, 0) == 0)
				continue;
			art.entrenar(rol);
			for (Cancion c : canciones) {
				if (cupos.getOrDefault(art, 0) <= 0) {
					break;
				}
				while (c.necesitaRol(rol) && cupos.getOrDefault(art, 0) > 0) {

					boolean yaContratadoEnEstaCancion = false;
					for (Contrato_x_Cancion cc : c.getContratos()) {

						if (cc.getArtista().equals(art)) {
							yaContratadoEnEstaCancion = true;
							break;
						}
					}

					if (yaContratadoEnEstaCancion) {
						break;
					}
					Contrato_x_Cancion contrato = new Contrato_x_Cancion(art, c, rol);
					c.agregarContrato(contrato);
					art.agregarContrato(contrato);
					cupos.put(art, cupos.get(art) - 1);
				}
			}
		}
	}

	public List<ArtistaData> obtenerDatosArtistasParaProlog() {
		List<ArtistaData> dataList = new ArrayList<>();

		for (Artista a : this.artistas) {
			if (a.contratosvacio() || a.esBase()) {
				String tipo = a.esBase() ? "base" : "no_base";
				ArtistaData artData = new ArtistaData(a.getNombre(), tipo, a.getRoles(), a.getCostoBase());
				dataList.add(artData);
			}
		}
		return dataList;
	}

	public List<CancionData> obtenerDatosCancionesParaProlog() {
		List<CancionData> dataList = new ArrayList<>();

		for (Cancion c : this.canciones) {
			Map<String, Integer> rolesFaltantes = c.rolesFaltantesConCantidad();
			List<String> rolesRequeridosFaltantes = new ArrayList<>();

			for (Map.Entry<String, Integer> rolfaltante : rolesFaltantes.entrySet()) {
				String rol = rolfaltante.getKey();
				int cantidad = rolfaltante.getValue();

				for (int i = 0; i < cantidad; i++) {
					rolesRequeridosFaltantes.add(rol);
				}
			}

			CancionData data = new CancionData(c.getTitulo(), rolesRequeridosFaltantes);
			dataList.add(data);
		}
		return dataList;
	}
}
