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

public class Menu {
	private Recital recital;
    private Scanner scanner;

    public Menu(Recital recital, Scanner scanner) {
        this.recital = recital;
        this.scanner = scanner;
    }

    public void mostrar() {
        int opcion;
        int opc2;
        do {
        	 System.out.println("=== MENÚ PRINCIPAL ===");
	            System.out.println("1. Ver roles faltantes de una canción");
	            System.out.println("2. Ver roles faltantes del recital");
	            System.out.println("3. Contratar artistas para una canción");
	            System.out.println("4. Contratar artistas para todas las canciones");
	            System.out.println("5. Entrenar artista");
	            System.out.println("6. Listar artistas contratados");
	            System.out.println("7. Listar canciones con su estado");
	            System.out.println("8. Quitar artista del recital");
	            System.out.println("9. Mostrar historial de colaboraciones");
	            System.out.println("0. Salir");
	            System.out.print("Elija una opción: ");

	            opcion = scanner.nextInt();
	            scanner.nextLine(); // limpiar buffer

	            switch (opcion) {
	                case 1 -> {
	                System.out.println("\n Elija una cancion: \n "+recital.getTituloCanciones());
	                opc2 = scanner.nextInt();
	                Map<String,Integer> rolesqFaltan= recital.rolesFaltantesCancionCantidad(opc2-1);
	                System.out.println("En la cancion " + opc2 +" faltan: ");
	                rolesqFaltan.forEach((titulo, cantidad) -> System.out.println("Titulo: "+titulo+" Cantidad: "+cantidad));
	                scanner.nextLine();
	                }
	                case 2 ->{
	                	Map<String, Integer> rolesFaltantesTodas = recital.rolesFaltantesTodasCanciones();
	                	System.out.println("-----ROLES QUE FALTAN EN TODO EL RECITAL-----");
	                	rolesFaltantesTodas.forEach((rol,cant)->System.out.println("Rol: "+rol+" Faltan:"+cant));
	                }
	                //case 3 -> 
	                //case 4 -> 
	                case 5 -> {
	                	entrenarArtista();
	                }
	                case 6 -> {
	                	listarArtistasContratados();
	                }
	                case 7 -> {
	                	listarCancionesConEstados();
	                }
	                case 8 -> {
	                	opcionArrepentimientoPorIndice();
	                }
	                case 9 -> {
	                	mostrarGrafoColaboraciones();
	                }
	                case 0 -> {
	                	exportarRecital();
	                	System.out.println("Saliendo del sistema...");
	                }
	                default -> System.out.println("Opción inválida");
	            }

        } while (opcion != 0);
    }
    
    //PUNTO 5
    public void entrenarArtista() {
    	int op;
    	String rol;
    	
    	do {
    		System.out.println("seleccione un artista de la lista para entrenar:");
    		System.out.println(recital.getArtistasFormato());
    		op = scanner.nextInt();
    		scanner.nextLine();    		
    	}while(op<1 || op>recital.getArtistas().size());
    	
    	Artista a = new Artista();
    	a = recital.getArtistas().get(op);
    	
    	if(a.esBase()) {
    		System.out.println("no se puede entrenar al artista, es un artista base.");
    		return;
    	}
    	
    	System.out.println("ingrese el rol para entrenar al artista: ");
    	rol = scanner.nextLine();
    	
    	if(((ArtistaInvitado) a).entrenar(rol)) {
    		System.out.println("el artista ha sido entrenado en el rol: " + rol);
    	} else {
    		System.out.println("no se pudo entrenar al artista. ya posee el rol o esta contratado.");
    	}    	
    }
    
    
    //PUNTO 6
	public void listarArtistasContratados() {
		double total = 0;
		
		System.out.println("\n=== LISTA DE ARTISTAS CONTRATADOS ===");
		System.out.printf("%-20s | %-20s | %-15s | %-10s%n","ARTISTA", "CANCION", "ROL", "COSTO");
	    System.out.println("----------------------------------------------------------------------------------");
	    
		for (Artista a : recital.getArtistas()) {
			double subtotal = 0;
			boolean primeraVez = true;
			if(a.tieneContratos()) {
				for (Contrato_x_Cancion c : a.getContratos()) {
					if (primeraVez) {
						System.out.printf("%-20s | %-20s | %-15s | $%-10.2f%n",
		                        a.getNombre(),
		                        c.getCancion().getTitulo(),  
		                        c.getRol(),
		                        c.getCosto());
						primeraVez = false;
					}
					else {
						System.out.printf("%-20s | %-20s | %-15s | $%-10.2f%n",
		                        "",
		                        c.getCancion().getTitulo(),
		                        c.getRol(),
		                        c.getCosto()
		                );
					}
					subtotal += c.getCosto();
				}
				System.out.printf("   Subtotal %-34s $%.2f%n", "", subtotal);
				System.out.println("----------------------------------------------------------------------------------");
				total += subtotal;	
			}	
		}
		System.out.println("Total: $" + total);
	}
	
	//PUNTO 7
	public void listarCancionesConEstados() {
		System.out.println("\n=== LISTA DE CANCIONES CON SU ESTADO ===");
		System.out.printf("%-33s | %-10s | %-10s | %-30s%n","CANCION", "ESTADO", "COSTO", "ROLES FALTANTES");
	    System.out.println("-------------------------------------------------------------------------------------------");
	    
	    for (Cancion cancion : recital.getCanciones()) {
	    	double costo = cancion.calcularCosto();
	    	boolean completa = cancion.tieneTodosLosRolesCubiertos();
	    	Map<String, Integer> faltantes = cancion.rolesFaltantesConCantidad();
	    	
	    	String estado = completa ? "COMPLETA" : "INCOMPLETA";
	    	
	    	StringBuilder faltantesTexto = new StringBuilder();//texto tipo: "voz principal (1), coro (2)"
	    	for (Map.Entry<String, Integer> entry : faltantes.entrySet()) {
	    		String rol = entry.getKey();
	    		int cant = entry.getValue();
	    		if (cant > 0) {
	    			if(faltantesTexto.length() > 0) {
	    				faltantesTexto.append(", ");
	    			}
	    			faltantesTexto.append(rol).append(" (").append(cant).append(")");
	    		}
	    	}
	    	
	    	if(faltantesTexto.length()==0) {
	    		faltantesTexto.append("-");
	    	}
	    	
	    	System.out.printf("%-33s | %-10s | $%-9.2f | %-30s%n",
	                cancion.getTitulo(),
	                estado,
	                costo,
	                faltantesTexto.toString()
	        );
	    }
	}
	//PUNTO 2 BONUS
//	private void mostrarArtistasConIndice() {
//		System.out.println("\n=== ARTISTAS DISPONIBLES ===");//usar si quiero quitar de la lista del recital
//		
//		List<Artista> artistas = recital.getArtistas();
//		for (int i = 0; i < artistas.size(); i++) {
//			Artista a = artistas.get(i);
//			System.out.printf("%d) %s%n", i + 1, a.getNombre());
//		}
//	}
	private List<Artista> mostrarArtistasContratadosConIndice() {// USAR EN opcionArrepentimientoPorIndice si solo se quiere sacar artistas con contrato
	    System.out.println("\n=== ARTISTAS CON CONTRATOS ===");

	    List<Artista> artistasConContratos = new ArrayList<>();
	    List<Artista> artistas = recital.getArtistas();
	    
	    int indiceVisible = 1;
	    for (Artista a : artistas) {
	    	if(a.tieneContratos()) {
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
		System.out.println("\n=== QUITAR ARTISTA DEL RECITAL ===");
	    List<Artista> artistasConContratos = mostrarArtistasContratadosConIndice();
	    
	    if(artistasConContratos.isEmpty()) {
	    	return;
	    }
	    
	    System.out.print("\nIngrese el índice del artista a quitar: ");
	    int indice = scanner.nextInt();
	    scanner.nextLine(); // limpiar ENTER
	    indice = indice - 1;//mostramos desde 1
	    
	    if(indice < 0 || indice >= artistasConContratos.size()) {
	    	System.out.println("Indice invalido.");
	    	return;
	    }
	    
	    Artista artista = artistasConContratos.get(indice);
	    
	    recital.quitarArtistaDelRecital(artista);
	    System.out.println("Se quitaron todos los contratos de " + artista.getNombre() + " del recital.");
	}
	
	
	//PUNTO 3 BONUS
	public void mostrarGrafoColaboraciones() {
	    System.out.println("\n=== HISTORIAL DE COLABORACIONES ===");
	    // Mapa: "Artista1|Artista2" -> cantidad de canciones compartidas
	    Map<String, Integer> colaboraciones = new HashMap<>();

	    for (Cancion cancion : recital.getCanciones()) {
	        // Obtener artistas que participan en esta canción
	        List<Artista> participantes = new ArrayList<>();
	        for (Contrato_x_Cancion c : cancion.getContratos()) {
	            Artista a = c.getArtista();
	            if (!participantes.contains(a)) {
	                participantes.add(a);
	            }
	        }

	        // Generar todos los pares (i,j) sin repetir
	        for (int i = 0; i < participantes.size(); i++) {
	            for (int j = i + 1; j < participantes.size(); j++) {

	                Artista a1 = participantes.get(i);
	                Artista a2 = participantes.get(j);

	                // Siempre ordenamos los nombres, así A|B y B|A son la misma clave
	                String nombre1 = a1.getNombre();
	                String nombre2 = a2.getNombre();

	                String clave = nombre1.compareTo(nombre2) < 0
	                        ? nombre1 + "|" + nombre2
	                        : nombre2 + "|" + nombre1;

	                colaboraciones.put(clave, colaboraciones.getOrDefault(clave, 0) + 1);
	            }
	        }
	    }

	    // Mostrar resultados
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

	        System.out.printf("%s ↔ %s (%d canción%s)\n",
	                a1,
	                a2,
	                cantidad,
	                cantidad > 1 ? "es" : ""
	        );
	    }
	}
	//PUNTO 5 BONUS
	private RecitalOutJson contruirRecitalOutDto(Recital recital) {
		RecitalOutJson out = new RecitalOutJson();
		out.canciones = new ArrayList<>();
		double totalRecital = 0.0;
		
		for (Cancion c : recital.getCanciones()) {
			CancionOutJson cOut =  new CancionOutJson();
			cOut.titulo = c.getTitulo();
			cOut.contratos = new ArrayList<>();
			
			double totalCancion = 0.0;
			
			for(Contrato_x_Cancion cx : c.getContratos()) {
				ContratoOutJson cxOut = new  ContratoOutJson();
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
	
	private void exportarRecital() {
		try {
			RecitalOutJson dto = contruirRecitalOutDto(recital);
			JsonIO.guardar(Path.of("data/recital-out.json"), dto);
			System.out.println("Se genero el archivo recital-out.json con el estado actual del recital.");
		} catch (IOException e) {
			System.out.println("Error al generar recital-out.json");
		}
	}
}



