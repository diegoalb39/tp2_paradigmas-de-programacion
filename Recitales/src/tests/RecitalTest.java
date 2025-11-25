package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import artistas.Artista;
import artistas.ArtistaInvitado;
import recitales.Cancion;
import recitales.Recital;

class RecitalTest {

	private Recital recital;
	private Cancion cancion1;
	private Cancion cancion2;
	private Artista cantanteBase;
	private Artista guitarristaBase;
	private ArtistaInvitado invitado;

	@BeforeEach
	void setUp() {
		cantanteBase = new Artista("Cantante Base", new ArrayList<>(Arrays.asList("cantante")), new ArrayList<>());

		guitarristaBase = new Artista("Guitarrista Base", new ArrayList<>(Arrays.asList("guitarra")),
				new ArrayList<>());

		invitado = new ArtistaInvitado("Invitado", new ArrayList<>(Arrays.asList("guitarra")), new ArrayList<>(), 800.0,
				2);

		List<String> roles1 = Arrays.asList("cantante", "guitarra");
		cancion1 = new Cancion("Tema 1", new ArrayList<>(roles1));
		cancion1.contratarArtista(cantanteBase, "cantante");

		List<String> roles2 = Arrays.asList("cantante");
		cancion2 = new Cancion("Tema 2", new ArrayList<>(roles2));

		List<Cancion> canciones = new ArrayList<>();
		canciones.add(cancion1);
		canciones.add(cancion2);

		List<Artista> artistas = new ArrayList<>();
		artistas.add(cantanteBase);
		artistas.add(guitarristaBase);
		artistas.add(invitado);

		recital = new Recital("Recital Test", canciones, artistas);
	}

	@Test
	void constructorYGettersBasicos() {
		assertEquals("Recital Test", recital.getNombre());
		assertEquals(2, recital.getCanciones().size());
		assertEquals(3, recital.getArtistas().size());
		assertSame(cancion1, recital.getCanciones().get(0));
		assertSame(cantanteBase, recital.getArtistas().get(0));
	}

	@Test
	void cuantosRolesFaltanCancionDelegadoACancion() {
		int faltan = recital.cuantosRolesFaltanCancion(0);
		assertEquals(1, faltan);
	}

	@Test
	void rolesFaltantesCancionCantidadDelegadoACancion() {
		List<String> roles = Arrays.asList("cantante", "guitarra", "guitarra");
		Cancion c = new Cancion("Multirol", new ArrayList<>(roles));

		c.contratarArtista(cantanteBase, "cantante");

		List<Cancion> canciones = new ArrayList<>();
		canciones.add(c);
		Recital rec = new Recital("Otro", canciones, new ArrayList<>(Arrays.asList(cantanteBase, guitarristaBase)));

		Map<String, Integer> faltantes = rec.rolesFaltantesCancionCantidad(0);

		assertEquals(2, faltantes.size());
		assertEquals(0, faltantes.get("cantante"));
		assertEquals(2, faltantes.get("guitarra"));
	}

	@Test
	void buscarArtistaPorNombreDevuelveNullSiNoExiste() {
		Artista encontrado = recital.busacarArtistaPorNombre("No Existe");
		assertNull(encontrado);
	}

	@Test
	void quitarArtistaDelRecitalRemueveContratosYNoDeLista() {
		assertTrue(recital.getArtistas().contains(cantanteBase));
		assertFalse(cantanteBase.getContratos().isEmpty());
		assertFalse(cancion1.getContratos().isEmpty());

		recital.quitarArtistaDelRecital(cantanteBase);

		assertTrue(recital.getArtistas().contains(cantanteBase));
		assertTrue(cantanteBase.getContratos().isEmpty());

		cancion1.getContratos().forEach(c -> assertNotEquals(cantanteBase, c.getArtista()));
		cancion2.getContratos().forEach(c -> assertNotEquals(cantanteBase, c.getArtista()));
	}

	@Test
	void quitarArtistaDelRecitalConNullNoModificaNada() {
		int cantArtistasAntes = recital.getArtistas().size();
		recital.quitarArtistaDelRecital(null);
		assertEquals(cantArtistasAntes, recital.getArtistas().size());
	}

	@Test
	void artistasBaseListaSoloDevuelveArtistasBase() {
		List<Artista> bases = recital.artistasBaseLista();

		assertEquals(2, bases.size());
		assertTrue(bases.contains(cantanteBase));
		assertTrue(bases.contains(guitarristaBase));
		assertFalse(bases.contains(invitado));
	}

	@Test
	void rolesFaltantesTodasCancionesTieneEnCuentaArtistasBase() {
		Artista baseCantante = new Artista("Base Cantante", new ArrayList<>(Arrays.asList("cantante")),
				new ArrayList<>());
		List<String> roles = Arrays.asList("cantante", "guitarra");
		Cancion c = new Cancion("Tema Completo", new ArrayList<>(roles));

		List<Cancion> canciones = new ArrayList<>();
		canciones.add(c);
		List<Artista> artistas = new ArrayList<>();
		artistas.add(baseCantante);

		Recital rec = new Recital("Recital Base", canciones, artistas);

		Map<String, Integer> faltantes = rec.rolesFaltantesTodasCanciones();

		assertEquals(1, faltantes.size());
		assertEquals(Integer.valueOf(1), faltantes.get("guitarra"));
		assertFalse(faltantes.containsKey("cantante"));
	}
}
