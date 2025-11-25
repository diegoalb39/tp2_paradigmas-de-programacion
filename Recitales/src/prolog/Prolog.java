package prolog;

import org.jpl7.Query;
import org.jpl7.Term;
import org.jpl7.Variable;
import java.util.List;
import java.util.Map;

public class Prolog {
	private static final String PROLOG_RULES_PATH = "prolog/reglas.pl";
	private boolean isPrologLoaded = false;

	public void inicializarProlog() {
		if (!isPrologLoaded) {
			String qRules = String.format("consult('%s')", PROLOG_RULES_PATH);

			if (new Query(qRules).hasSolution()) {
				isPrologLoaded = true;
			} else {
				System.err.println("ERROR: No se pudo cargar el archivo de reglas. Verifique las librerías JPL.");
			}
		}
	}

	/// EJERCICIO PROLOG

	public int calcularMinimoEntrenamientos(List<ArtistaData> artistas, List<CancionData> canciones) {

		if (!isPrologLoaded) {
			System.err.println("Prolog no inicializado. Llame a inicializarProlog() primero.");
			return -1;
		}

		// Eliminar hechos de la ejecución anterior.

		try {
			new Query("retractall(artista(_, _, _))").hasSolution();
			new Query("retractall(artista_rol(_, _))").hasSolution();
			new Query("retractall(cancion_rol(_, _))").hasSolution();

		} catch (Exception e) {
			System.err.println("Fallo al limpiar la base dinámica.");
		}
		/// Insertar datos de Java en Prolog.

		// Insertar Artistas (nombre, tipo, costo) y Roles (nombre, rol)

		for (ArtistaData a : artistas) {
			String nombreProlog = a.getNombre().toLowerCase().replace(" ", "_");
			String tipoProlog = a.getTipo().toLowerCase().replace(" ", "_");
			String costoProlog = String.valueOf(a.getCosto());

			String assertArtista = String.format("assertz(artista(%s, %s, %s))", nombreProlog, tipoProlog, costoProlog);
			new Query(assertArtista).hasSolution();

			for (String rol : a.getRoles()) {
				String rolProlog = rol.toLowerCase().replace(" ", "_");
				String assertRol = String.format("assertz(artista_rol(%s, %s))", nombreProlog, rolProlog);
				new Query(assertRol).hasSolution();
			}
		}

		// Insertar Roles por Canción

		for (CancionData c : canciones) {
			String cancionProlog = c.getNombre().toLowerCase().replace(" ", "_");
			for (String rol : c.getRolesRequeridos()) {
				String rolProlog = rol.toLowerCase().replace(" ", "_");

				String assertRol = String.format("assertz(cancion_rol(%s, %s))", cancionProlog, rolProlog);
				new Query(assertRol).hasSolution();
			}
		}

		/// CONSULTA ///

		Variable total = new Variable("Total");
		Query consulta = new Query("entrenamientos_minimos", new Term[] { total });

		if (consulta.hasSolution()) {
			Map<String, Term> solucion = consulta.oneSolution();
			Term valorTotal = solucion.get("Total");
			if (valorTotal != null && valorTotal.isInteger()) {
				return valorTotal.intValue();
			}
		}
		return 0;
	}

}
