package recitales;
import java.util.Scanner;

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
	                System.out.println("\n Elija una cancion: \n "+recital.getCanciones());
	                opc2 = scanner.nextInt();
	                int rolesqFaltan= recital.verRolesFaltantesCancion(opc2-1);
	                System.out.println("Faltan "+ rolesqFaltan + " roles en la cancion " + opc2);
	                scanner.nextLine();
	                }
	                case 2 ->{
	                	System.out.println("Faltan "+recital.rolesFaltantesEnTodas()+" en todas las canciones.");
	                }
	                //case 3 -> 
	                //case 4 -> 
	                //case 5 -> 
	                //case 6 -> 
	                //case 7 -> 
	                case 0 -> System.out.println("Saliendo del sistema...");
	                default -> System.out.println("Opción inválida");
	            }

        } while (opcion != 0);
    }
}
