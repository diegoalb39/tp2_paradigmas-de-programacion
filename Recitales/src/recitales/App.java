package recitales;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class App {
	
	public static void main(String[] args) {
			
			List<String> roles = new ArrayList<String>();
			roles.add("Guitarra");
			roles.add("Armonica");
			roles.add("Voz");
			roles.add("Acordeon");
			List<String> roles2 = new ArrayList<String>();
			roles2.add("Bajo");
			roles2.add("Guitarra");
			roles2.add("Voz");
	
			List<String> roles3 = new ArrayList<String>();
			roles3.add("Bajo");
			roles3.add("Guitarra");
		
			Cancion cancion1 = new Cancion("Alla en Tilcara",roles);
			Cancion cancion2 = new Cancion("Intifada",roles2);
			Cancion cancion3 = new Cancion("De la cabeza",roles3);
			
			List<Cancion> canciones = new ArrayList<Cancion>();
			canciones.add(cancion1);
			canciones.add(cancion2);
			canciones.add(cancion3);
			
			Recital recital = new Recital("uno",canciones);
			
			Scanner scanner = new Scanner(System.in);
		  Menu menu = new Menu(recital, scanner);
		    menu.mostrar();
	       
	}
	
}
