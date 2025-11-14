package recitales;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
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
		/*// ====== IMPORTACION DE ARCHIVOS ====
		List<ArtistaJson> artistasJson = JsonIO.cargarArtistas(Path.of("data/artistas.json"));
		List<String> baseNombresJson = JsonIO.cargarArtistasBase(Path.of("data/artistas-discografica.json"));
		List<CancionJson> cancionesJson = JsonIO.cargarCanciones(Path.of("data/recital.json"));
		
		System.out.println("===== ARTISTAS =====");
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
		
		//TODO Pasar el contendo de las clases Json a las clases normales
		
		List<String> roles = new ArrayList<String>();
		roles.add("Guitarra");
		roles.add("Armonica");
		roles.add("Guitarra");
		roles.add("Voz");
		roles.add("Voz");
		roles.add("Acordeon");
		List<String> roles2 = new ArrayList<String>();
		roles2.add("Bajo");
		roles2.add("Guitarra");
		roles2.add("Voz");

		List<String> roles3 = new ArrayList<String>();
		roles3.add("Voz");

		Cancion cancion1 = new Cancion("Alla en Tilcara", roles);
		Cancion cancion2 = new Cancion("Intifada", roles2);
		Cancion cancion3 = new Cancion("De la cabeza", roles3);

		List<Cancion> canciones = new ArrayList<Cancion>();
		canciones.add(cancion1);
		canciones.add(cancion2);
		canciones.add(cancion3);

		List<Artista> artistas = new ArrayList<>();
		List<Banda> bandas = new ArrayList<>();

		Banda banda1 = new Banda("bandana", artistas);
		bandas.add(banda1);

		Artista art1 = new Artista("Marco Antonio Solis", roles3, bandas);
		Artista art2 = new Artista("Leon Gieco", roles2, bandas);
		ArtistaInvitado artInv = new ArtistaInvitado("Wos", roles, bandas, 100, 1);
		artistas.add(art1);
		artistas.add(art2);

		artistas.add(artInv);
		artistas.add(art1);

		System.out.println(cancion1.contratarArtista(art1, "Voz"));
		System.out.println(cancion1.contratarArtista(art2, "Voz"));
		System.out.println(cancion1.contratarArtista(artInv, "Voz"));

		Recital recital = new Recital("uno", canciones, artistas);

		Scanner scanner = new Scanner(System.in);
		Menu menu = new Menu(recital, scanner);
		menu.mostrar();

	}

}
