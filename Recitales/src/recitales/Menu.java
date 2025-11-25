package recitales;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import artistas.Artista;
import artistas.ArtistaInvitado;
import io.CancionOutJson;
import io.ContratoOutJson;
import io.JsonIO;
import io.RecitalOutJson;

import prolog.Prolog;
import prolog.ArtistaData;
import prolog.CancionData;

public class Menu {
	private Recital recital;
	private Scanner scanner;
	private Prolog p;

	public Menu(Recital recital, Scanner scanner) {
		this.recital = recital;
		this.scanner = scanner;
		this.p = new Prolog();
		this.p.inicializarProlog();
	}

	public void mostrar() {
		int opcion;
		do {
			System.out.println("\n=============== MENÚ PRINCIPAL ===============");
			System.out.println("1. Ver roles faltantes de una canción");
			System.out.println("2. Ver roles faltantes del recital");
			System.out.println("3. Contratar artistas para una canción");
			System.out.println("4. Contratar artistas para todas las canciones");
			System.out.println("5. Entrenar artista");
			System.out.println("6. Listar artistas contratados");
			System.out.println("7. Listar canciones con su estado");
			System.out.println("8. Quitar artista del recital");
			System.out.println("9. Mostrar historial de colaboraciones");
			System.out.println("10. Entrenamientos minimos para cubrir todos los roles [Prolog]");
			System.out.println("0. Salir");
			System.out.println("==============================================");
			System.out.print("Elija una opción: ");

			opcion = scanner.nextInt();
			scanner.nextLine();

			switch (opcion) {
			case 1 -> {
				rolesFaltantesParaUnaCancion();
			}
			case 2 -> {
				rolesFaltanteParaTodasLasCanciones();
			}
			case 3 -> {
				contratarArtistaParaCancion();
			}
			case 4 -> {
				contratarArtistasParaRecital();
			}
			case 5 -> {
				entrenarArtista();
			}
			case 6 -> {
				listarArtistasContratados();
			}
			case 7 -> {
				listarCancionesConEstado();
			}
			case 8 -> {
				opcionArrepentimientoPorIndice();
			}
			case 9 -> {
				mostrarGrafoColaboraciones();
			}
			case 10 -> {
				calcularEntrenamientosMinimosProlog();
			}
			case 0 -> {
				exportarRecital();
				System.out.println("Saliendo del sistema...");
			}
			default -> System.out.println("Opción inválida");
			}

			if (opcion != 0) {
				System.out.println("\nPresione ENTER para continuar...");
				scanner.nextLine();
				limpiarPantalla();
			}

		} while (opcion != 0);
	}

	static void limpiarPantalla() {
		System.out.print("\033[H\033[2J");
		System.out.flush();
	}

	// PUNTO 1
	public void rolesFaltantesParaUnaCancion() {
		System.out.println("\n===== ROLES FALTANTES PARA UNA CANCION =====");
		System.out.println("Elija una cancion: \n" + recital.getTituloCanciones());
		int opc2 = scanner.nextInt();
		Map<String, Integer> rolesqFaltan = recital.rolesFaltantesCancionCantidad(opc2 - 1);
		System.out.println("En la cancion " + opc2 + " faltan: ");
		rolesqFaltan.forEach((rol, cantidad) -> System.out.println("Rol: " + rol + " - Cantidad: " + cantidad));
		scanner.nextLine();
	}

	// PUNTO 2
	public void rolesFaltanteParaTodasLasCanciones() {
		Map<String, Integer> rolesFaltantesTodas = recital.rolesFaltantesTodasCanciones();
		System.out.println("\n===== ROLES QUE FALTAN EN TODO EL RECITAL =====");
		rolesFaltantesTodas.forEach((rol, cant) -> System.out.println("Rol: " + rol + " - Faltan:" + cant));
	}

	// PUNTO 3
	private void contratarArtistaParaCancion() {
		int opc2;

		List<Cancion> canciones = recital.getCancionesIncompletas();
		boolean rta = true;
		System.out.println("\n===== CONTRATAR ARTISTAS PARA UNA CANCION =====");
		if (canciones.isEmpty()) {
			System.out.println("\nTodas las canciones ya están completas.");
			return;
		}
		System.out.println("Elija una cancion:");
		for (int i = 0; i < canciones.size(); i++) {
			System.out.println((i + 1) + ") " + canciones.get(i).getTitulo());

		}
		opc2 = scanner.nextInt();
		rta = recital.contratarParaCancion(canciones.get(opc2 - 1));

		System.out.println("Artistas contratados");
		if (rta && recital.cuantosRolesFaltanCancion(opc2) > 0) {
			Map<ArtistaInvitado, String> sugeridos = recital.buscarEntrenables(recital.getCancion(opc2 - 1));

			if (!sugeridos.isEmpty()) {
				System.out.println("\nSe recomienda entrenar:");
				for (Map.Entry<ArtistaInvitado, String> e : sugeridos.entrySet()) {
					System.out.println(e.getKey().getNombre() + " para rol " + e.getValue());
				}

				System.out.println("\n¿Desea entrenarlos y contratarlos? (0 = No, 1 = Si): ");
				int r = scanner.nextInt();

				if (r == 1) {
					recital.entrenarArtistasYContratar(sugeridos, recital.getCancion(opc2 - 1));
				}
			}
		}

		System.out.println("CONTRATOS FINALES:");
		canciones.get(opc2 - 1).mostrarContratos();
	}

	// PUNTO 4
	private void contratarArtistasParaRecital() {

		boolean rta = true;
		System.out.println("\n===== CONTRATAR ARTISTAS PARA TODAS LAS CANCIONES =====");
		List<Cancion> canciones = recital.getCancionesIncompletas();
		if (canciones.isEmpty()) {
			System.out.println("\nTodas las canciones ya están completas.");
			return;
		}
		rta = recital.contratarParaRecital();
		System.out.println("Artistas contratados");

		if (rta && recital.cuantosRolesFaltanEnTodas() > 0) {

			Map<ArtistaInvitado, String> sugeridos = recital.buscarEntrenablesReci();

			if (!sugeridos.isEmpty()) {

				System.out.println("\nSe recomienda entrenar para el recital:");

				for (Map.Entry<ArtistaInvitado, String> e : sugeridos.entrySet()) {
					System.out.println(e.getKey().getNombre() + " para rol " + e.getValue());
				}

				System.out.println("\n¿Desea entrenarlos y contratarlos? (0=No, 1=Sí): ");
				int r = scanner.nextInt();

				if (r == 1) {
					recital.entrenarArtistasYContratarRecital(sugeridos);
				}
			}
		}

		List<Cancion> todas = recital.getCanciones();
		for (int i = 0; i < todas.size(); i++) {
			Cancion c = todas.get(i);

			System.out.println("Canción " + (i + 1) + ": " + c.getTitulo());
			c.mostrarContratos();
			System.out.println("--------------------------------------\n");
		}

		return;
	}

	// PUNTO 5
	public void entrenarArtista() {
		int op;
		String rol;
		System.out.println("\n===== ENTRENAR ARTISTA =====");
		do {
			System.out.println("Seleccione un artista de la lista para entrenar:");
			System.out.println(recital.getArtistasFormato());
			op = scanner.nextInt();
			scanner.nextLine();
		} while (op < 1 || op > recital.getArtistas().size());

		Artista a = new Artista();
		a = recital.getArtistas().get(op - 1);

		if (a.esBase()) {
			System.out.println("No se puede entrenar al artista, es un artista base.");
			return;
		}

		System.out.println("Ingrese el rol para entrenar al artista: ");
		rol = scanner.nextLine();

		if (((ArtistaInvitado) a).entrenar(rol)) {
			System.out.println("El artista ha sido entrenado en el rol: " + rol);
		} else {
			System.out.println("No se pudo entrenar al artista. ya posee el rol o esta contratado.");
		}
	}

	// PUNTO 6
	public void listarArtistasContratados() {
		double total = 0;

		System.out.println("\n===== LISTA DE ARTISTAS CONTRATADOS =====");
		System.out.printf("%-20s | %-20s | %-15s | %-10s%n", "ARTISTA", "CANCION", "ROL", "COSTO");
		System.out.println("----------------------------------------------------------------------------------");

		for (Artista a : recital.getArtistas()) {
			double subtotal = 0;
			boolean primeraVez = true;
			if (a.tieneContratos()) {
				for (Contrato_x_Cancion c : a.getContratos()) {
					if (primeraVez) {
						System.out.printf("%-20s | %-20s | %-15s | $%-10.2f%n", a.getNombre(),
								c.getCancion().getTitulo(), c.getRol(), c.getCosto());
						primeraVez = false;
					} else {
						System.out.printf("%-20s | %-20s | %-15s | $%-10.2f%n", "", c.getCancion().getTitulo(),
								c.getRol(), c.getCosto());
					}
					subtotal += c.getCosto();
				}
				System.out.printf("   Subtotal: $%.2f\n", subtotal);
				System.out
						.println("----------------------------------------------------------------------------------");
				total += subtotal;
			}
		}
		System.out.println("Total: $" + total);
	}

	// PUNTO 7
	public void listarCancionesConEstado() {
		System.out.println("\n===== LISTA DE CANCIONES CON SU ESTADO =====");
		System.out.printf("%-33s | %-10s | %-10s | %-30s%n", "CANCION", "ESTADO", "COSTO", "ROLES FALTANTES");
		System.out
				.println("-------------------------------------------------------------------------------------------");

		for (Cancion cancion : recital.getCanciones()) {
			double costo = cancion.calcularCosto();
			boolean completa = cancion.tieneTodosLosRolesCubiertos();
			Map<String, Integer> faltantes = cancion.rolesFaltantesConCantidad();

			String estado = completa ? "COMPLETA" : "INCOMPLETA";

			StringBuilder faltantesTexto = new StringBuilder();
			for (Map.Entry<String, Integer> entry : faltantes.entrySet()) {
				String rol = entry.getKey();
				int cant = entry.getValue();
				if (cant > 0) {
					if (faltantesTexto.length() > 0) {
						faltantesTexto.append(", ");
					}
					faltantesTexto.append(rol).append(" (").append(cant).append(")");
				}
			}

			if (faltantesTexto.length() == 0) {
				faltantesTexto.append("-");
			}

			System.out.printf("%-33s | %-10s | $%-9.2f | %-30s%n", cancion.getTitulo(), estado, costo,
					faltantesTexto.toString());
		}
	}

	// PUNTO 2 BONUS
	private List<Artista> mostrarArtistasContratadosConIndice() {
		System.out.println("Artistas con contratos:");

		List<Artista> artistasConContratos = new ArrayList<>();
		List<Artista> artistas = recital.getArtistas();

		int indiceVisible = 1;
		for (Artista a : artistas) {
			if (a.tieneContratos()) {
				System.out.printf("%d) %s%n", indiceVisible, a.getNombre());
				artistasConContratos.add(a);
				indiceVisible++;
			}
		}

		if (artistasConContratos.isEmpty()) {
			System.out.println("No hay artistas con contratos en el recital.");
		}

		return artistasConContratos;
	}

	public void opcionArrepentimientoPorIndice() {
		System.out.println("\n===== QUITAR ARTISTA DEL RECITAL =====");
		List<Artista> artistasConContratos = mostrarArtistasContratadosConIndice();

		if (artistasConContratos.isEmpty()) {
			return;
		}

		System.out.print("\nIngrese el artista a quitar: ");
		int indice = scanner.nextInt();
		scanner.nextLine();
		indice = indice - 1;

		if (indice < 0 || indice >= artistasConContratos.size()) {
			System.out.println("Indice invalido.");
			return;
		}

		Artista artista = artistasConContratos.get(indice);

		recital.quitarArtistaDelRecital(artista);
		System.out.println("Se quitaron todos los contratos de " + artista.getNombre() + " del recital.");
	}

	// PUNTO 3 BONUS
	public void mostrarGrafoColaboraciones() {
		System.out.println("\n===== HISTORIAL DE COLABORACIONES =====");
		Map<String, Integer> colaboraciones = new HashMap<>();

		for (Cancion cancion : recital.getCanciones()) {
			List<Artista> participantes = new ArrayList<>();
			for (Contrato_x_Cancion c : cancion.getContratos()) {
				Artista a = c.getArtista();
				if (!participantes.contains(a)) {
					participantes.add(a);
				}
			}
			for (int i = 0; i < participantes.size(); i++) {
				for (int j = i + 1; j < participantes.size(); j++) {

					Artista a1 = participantes.get(i);
					Artista a2 = participantes.get(j);

					String nombre1 = a1.getNombre();
					String nombre2 = a2.getNombre();

					String clave = nombre1.compareTo(nombre2) < 0 ? nombre1 + "|" + nombre2 : nombre2 + "|" + nombre1;
					colaboraciones.put(clave, colaboraciones.getOrDefault(clave, 0) + 1);
				}
			}
		}

		if (colaboraciones.isEmpty()) {
			System.out.println("No hay colaboraciones registradas.");
			return;
		}
		for (Map.Entry<String, Integer> entry : colaboraciones.entrySet()) {
			String clave = entry.getKey();
			int cantidad = entry.getValue();

			String[] artistas = clave.split("\\|");
			String a1 = artistas[0];
			String a2 = artistas[1];

			System.out.printf("%s ↔ %s (%d canción%s)\n", a1, a2, cantidad, cantidad > 1 ? "es" : "");
		}
	}

	// PUNTO 5 BONUS
	private RecitalOutJson contruirRecitalOutDto(Recital recital) {
		RecitalOutJson out = new RecitalOutJson();
		out.canciones = new ArrayList<>();
		double totalRecital = 0.0;

		for (Cancion c : recital.getCanciones()) {
			CancionOutJson cOut = new CancionOutJson();
			cOut.titulo = c.getTitulo();
			cOut.contratos = new ArrayList<>();

			double totalCancion = 0.0;

			for (Contrato_x_Cancion cx : c.getContratos()) {
				ContratoOutJson cxOut = new ContratoOutJson();
				cxOut.artista = cx.getArtista().getNombre();
				cxOut.rol = cx.getRol();
				cxOut.costo = cx.getCosto();

				cOut.contratos.add(cxOut);
				totalCancion += cxOut.costo;
			}

			cOut.total = totalCancion;

			String estado = c.tieneTodosLosRolesCubiertos() ? "completa" : "incompleta";
			cOut.estado = estado;

			out.canciones.add(cOut);
			totalRecital += totalCancion;
		}

		out.totalRecital = totalRecital;
		return out;
	}
	
	public void exportarRecital() {
		try {
			RecitalOutJson dto = contruirRecitalOutDto(recital);
			JsonIO.guardar(Path.of("data/recital-out.json"), dto);
			System.out.println("Se genero el archivo recital-out.json con el estado actual del recital.");
		} catch (IOException e) {
			System.out.println("Error al generar recital-out.json");
		}
	}
	
	// PUNTO 10
	private void calcularEntrenamientosMinimosProlog() {
		System.out.println("\n===== Entrenamientos minimos para cubrir todos los roles =====");
		
		List<ArtistaData> artistasData = recital.obtenerDatosArtistasParaProlog();
		List<CancionData> cancionesData = recital.obtenerDatosCancionesParaProlog();
		
		int entrenamientos = p.calcularMinimoEntrenamientos(artistasData, cancionesData);
		System.out.printf("El mínimo de artistas a entrenar es: %d\n", entrenamientos);
	}
}
