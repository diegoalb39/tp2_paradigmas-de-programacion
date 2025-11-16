package recitales;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import artistas.Artista;
import artistas.ArtistaInvitado;
import io.JsonIO;
import io.ArtistaJson;
import io.CancionJson;

public class App {

	public static void main(String[] args) throws IOException {
		// ====== IMPORTACION DE ARCHIVOS ====
		List<ArtistaJson> artistasJson = JsonIO.cargarArtistas(Path.of("data/artistas.json"));
		List<String> baseNombresJson = JsonIO.cargarArtistasBase(Path.of("data/artistas-discografica.json"));
		List<CancionJson> cancionesJson = JsonIO.cargarCanciones(Path.of("data/recital.json"));
		
		/*System.out.println("===== ARTISTAS =====");
		for (ArtistaJson a : artistasJson) {
			System.out.println(a.toString());
		}
		System.out.println("===== ARTISTAS BASE =====");
		for (String s : baseNombresJson) {
			System.out.println(s);
		}
		System.out.println("===== CANCIONES =====");
		for (CancionJson c : cancionesJson) {
			System.out.println(c.toString());
		}
		// ===================================*/
		
		// === Crear bandas ===
		List<Banda> bandas = new ArrayList<>();
		for (ArtistaJson aj : artistasJson) {
			for(String nombreBanda : aj.getBandas()) {
				boolean yaExiste = false;
				for(Banda b : bandas) {
					if(b.getNombre().equalsIgnoreCase(nombreBanda)) {
						yaExiste = true;
						break;
					}
				}
				if(!yaExiste) {
					bandas.add(new Banda(nombreBanda, new ArrayList<>()));
				}
			}
		}
		System.out.println(bandas);
		// === Crear artistas ===
		List<Artista> artistas = new ArrayList<>();
		for (ArtistaJson aj : artistasJson) {
		    artistas.add(convertirAArtista(aj, bandas));
		}


		// === Cargar artistas dentro de sus bandas ===
		for (Banda banda : bandas) {
		    for (Artista artista : artistas) {
		        if (artista.getBandas().stream()
		                .anyMatch(b -> b.getNombre().equalsIgnoreCase(banda.getNombre()))) {

		            banda.getIntegrantes().add(artista);
		        }
		    }
		}


		// === Crear canciones ===
		List<Cancion> canciones = new ArrayList<>();
		for (CancionJson cj : cancionesJson) {
		    canciones.add(convertirACancion(cj));
		}
		//bandas prueba
		//System.out.println(bandas);
		System.out.println(artistas.get(7).getBandas());
		System.out.println(artistas.get(0).getBandas());
//		//CONTRATO FEO
		canciones.get(0).contratarArtista(artistas.get(0), "guitarra eléctrica");
		canciones.get(1).contratarArtista(artistas.get(4), "voz principal");
		canciones.get(0).contratarArtista(artistas.get(4), "piano");	
		canciones.get(0).contratarArtista(artistas.get(1), "batería");
		canciones.get(0).contratarArtista(artistas.get(2), "bajo");
		canciones.get(0).contratarArtista(artistas.get(7), "voz principal");
		
		System.out.println(artistas.get(7).getCostoBase());
		
		//----------


		// === Crear recital ===
		Recital recital = new Recital("Recital Principal", canciones, artistas);
		
		for(int i=0;i<artistas.size();i++) {
			System.out.println(artistas.get(i));
		}
		
		// === Mostrar menú ===
		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu(recital, scanner);
		menu.mostrar();
	}

	private static Artista convertirAArtista(ArtistaJson json, List<Banda> bandasReales) {
	    // Encontrar todas las bandas reales que coincidan con las del JSON
	    List<Banda> bandasDelArtista = new ArrayList<>();
	    for (String nombreBanda : json.getBandas()) {
	        bandasReales.stream()
	            .filter(b -> b.getNombre().equalsIgnoreCase(nombreBanda))
	            .findFirst()
	            .ifPresent(bandasDelArtista::add);
	    }

	    // Si tiene costo → es ArtistaInvitado
	    if (json.getCosto() > 0) {
	        return new ArtistaInvitado(
	            json.getNombre(),
	            json.getRoles(),
	            bandasDelArtista,
	            json.getCosto(),
	            json.getMaxCanciones()
	        );
	    }

	    // Si no, es Artista normal
	    return new Artista(
	        json.getNombre(),
	        json.getRoles(),
	        bandasDelArtista
	    );
	}
	
	private static Cancion convertirACancion(CancionJson json) {
	    return new Cancion(
	        json.getTitulo(),
	        json.getRolesRequeridos()
	    );
	}
	
	
}
