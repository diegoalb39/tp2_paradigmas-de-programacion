package recitales;
import java.util.Map;
import java.util.Scanner;

import artistas.Artista;

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
	            System.out.println("9. Salir");
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
	                	System.out.println("Roles que faltan "+recital.rolesFaltantesEnTodas()+" en todas las canciones.");
	                }
	                //case 3 -> 
	                //case 4 -> 
	                //case 5 -> 
	                case 6 -> {
	                	listarArtistasContratados();
	                }
	                case 7 -> {
	                	listarCancionesConEstados();
	                }
	                case 0 -> System.out.println("Saliendo del sistema...");
	                default -> System.out.println("Opción inválida");
	            }

        } while (opcion != 0);
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
}
