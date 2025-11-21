package recitales;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import artistas.Artista;
import artistas.ArtistaInvitado;
import io.JsonIO;
import io.ArtistaJson;
import io.CancionJson;

public class App {

	public static void main(String[] args) throws IOException {
		// === Importacion de Json===
		List<ArtistaJson> artistasJson = JsonIO.cargarArtistas(Path.of("data/artistas.json"));
		List<String> baseNombresJson = JsonIO.cargarArtistasBase(Path.of("data/artistas-discografica.json"));
		List<CancionJson> cancionesJson = JsonIO.cargarCanciones(Path.of("data/recital.json"));

		// === Crear bandas ===
		List<Banda> bandas = new ArrayList<>();
		for (ArtistaJson aj : artistasJson) {
			for (String nombreBanda : aj.getBandas()) {
				boolean yaExiste = false;
				for (Banda b : bandas) {
					if (b.getNombre().equalsIgnoreCase(nombreBanda)) {
						yaExiste = true;
						break;
					}
				}
				if (!yaExiste) {
					bandas.add(new Banda(nombreBanda, new ArrayList<>()));
				}
			}
		}

		// === Crear artistas ===
		List<Artista> artistas = new ArrayList<>();
		for (ArtistaJson aj : artistasJson) {
			artistas.add(convertirAArtista(aj, bandas, baseNombresJson));
		}

		// === Cargar artistas dentro de sus bandas ===
		for (Banda banda : bandas) {
			for (Artista artista : artistas) {
				if (artista.getBandas().stream().anyMatch(b -> b.getNombre().equalsIgnoreCase(banda.getNombre()))) {
					banda.getIntegrantes().add(artista);
				}
			}
		}

		// === Crear canciones ===
		List<Cancion> canciones = new ArrayList<>();
		for (CancionJson cj : cancionesJson) {
			canciones.add(convertirACancion(cj));
		}

		// === Crear recital ===
		Recital recital = new Recital("Recital Principal", canciones, artistas);

		// === Mostrar menú ===
		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu(recital, scanner);
		menu.mostrar();
	}

	private static Artista convertirAArtista(ArtistaJson json, List<Banda> bandasReales, List<String> bases) {
		List<Banda> bandasDelArtista = new ArrayList<>();
		for (String nombreBanda : json.getBandas()) {
			bandasReales.stream().filter(b -> b.getNombre().equalsIgnoreCase(nombreBanda)).findFirst()
					.ifPresent(bandasDelArtista::add);
		}

		boolean esBase = bases.contains(json.getNombre());
		if (esBase) {
			return new Artista(json.getNombre(), json.getRoles(), bandasDelArtista);
		}
		return new ArtistaInvitado(json.getNombre(), json.getRoles(), bandasDelArtista, json.getCosto(),
				json.getMaxCanciones());
	}

	private static Cancion convertirACancion(CancionJson json) {
		return new Cancion(json.getTitulo(), json.getRolesRequeridos());
	}
}
