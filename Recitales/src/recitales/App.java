package recitales;

import java.util.Scanner;

public class App {
	
	public static void main(String[] args) {
			Recital recital = new Recital("uno");
		  Scanner scanner = new Scanner(System.in);
		  Menu menu = new Menu(recital, scanner);
		    menu.mostrar();
	       
	}
	
}
