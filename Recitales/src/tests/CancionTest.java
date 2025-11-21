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
import recitales.Banda;
import recitales.Cancion;
import recitales.Contrato_x_Cancion;

class CancionTest {
	private Cancion cancionBase;
	private List<String> rolesBase;

	@BeforeEach
	public void setUp() {
		rolesBase = Arrays.asList("cantante", "guitarra", "bateria");
		cancionBase = new Cancion("Tema 1", new ArrayList<>(rolesBase));
	}

	@Test
	public void constructorInicializaTituloRolesYContratosVacio() {
		assertEquals("Tema 1", cancionBase.getTitulo());
		assertEquals(rolesBase, cancionBase.getRoles());
		assertTrue(cancionBase.getContratos().isEmpty());
	}

	@Test
	public void contratarArtistaExitoso() {
		Artista cantante = new Artista("Juan", Arrays.asList("cantante"), new ArrayList<Banda>());
		boolean resultado = cancionBase.contratarArtista(cantante, "cantante");

		assertTrue(resultado, "Deberia poder contratar al artista");
		assertEquals(1, cancionBase.getContratos().size(), "Debe haber un solo contrato");

		Contrato_x_Cancion contrato = cancionBase.getContratos().get(0);
		assertSame(cantante, contrato.getArtista(), "El contrato debe guardar el artista correcto");
		assertEquals("cantante", contrato.getRol(), "El rol del contrato debe coincidir");
		assertEquals(0.0, contrato.getCosto(), "El costo del contrato debe ser el costo base del artista");
		assertTrue(cantante.getContratos().contains(contrato), "El artista tambien debe conocer su contrato");
	}

	@Test
	public void contratarArtistaFallaPorRolInexistenteEnCancion() {
		Artista tecladista = new Artista("Ana", Arrays.asList("teclado"), new ArrayList<Banda>());
		boolean resultado = cancionBase.contratarArtista(tecladista, "teclado");

		assertFalse(resultado, "No se puede contratar un rol que la cancion no necesita");
		assertTrue(cancionBase.getContratos().isEmpty(), "No debe generarse contratos");
		assertTrue(tecladista.getContratos().isEmpty(), "El artista tampoco debe tener contratos");
	}

	@Test
	public void contratarArtistaFallaPorArtistaNoTieneEseRol() {
		Artista guitarrista = new Artista("Luis", Arrays.asList("guitarra"), new ArrayList<Banda>());
		boolean resultado = cancionBase.contratarArtista(guitarrista, "cantante");

		assertFalse(resultado, "No se puede contratar un artista que no tiene ese rol");
		assertTrue(cancionBase.getContratos().isEmpty(), "No debe generarse contratos");
	}

	@Test
	public void contratarArtistaFallaPorArtistaRepetidoEnLaCancion() {
		Artista cantante = new Artista("Juan", Arrays.asList("cantante"), new ArrayList<Banda>());

		assertTrue(cancionBase.contratarArtista(cantante, "cantante"));
		assertEquals(1, cancionBase.getContratos().size());

		boolean segundoResultado = cancionBase.contratarArtista(cantante, "guitarra");
		assertFalse(segundoResultado, "El mismo artista no deberia poder contratarse dos veces en la misma cancion");
		assertEquals(1, cancionBase.getContratos().size(), "No debe haberse agregado un nuevo contrato");
	}

	@Test
	public void contratarArtistaFallaPorRolNoDisponible() {
		List<String> roles = Arrays.asList("guitarra", "guitarra");
		Cancion cancion = new Cancion("Tema 2", new ArrayList<>(roles));

		Artista g1 = new Artista("g1", Arrays.asList("guitarra"), new ArrayList<Banda>());
		Artista g2 = new Artista("g2", Arrays.asList("guitarra"), new ArrayList<Banda>());
		Artista g3 = new Artista("g3", Arrays.asList("guitarra"), new ArrayList<Banda>());

		assertTrue(cancion.contratarArtista(g1, "guitarra"));
		assertTrue(cancion.contratarArtista(g2, "guitarra"));

		boolean resultadoTercero = cancion.contratarArtista(g3, "guitarra");
		assertFalse(resultadoTercero, "No deberia poder contratar un tercer guitarrista");
		assertEquals(2, cancion.getContratos().size());
	}

	@Test
	public void rolesFaltantesCuentaTotalCorrectamente() {
		assertEquals(rolesBase.size(), cancionBase.rolesFaltantes());

		Artista cantante = new Artista("Juan", Arrays.asList("cantante"), new ArrayList<Banda>());
		cancionBase.contratarArtista(cantante, "cantante");

		assertEquals(2, cancionBase.rolesFaltantes(), "Luego de contratar un rol debe faltar 2");
	}

	@Test
	public void rolesFaltantesConCantidadPorCadaRol() {
		List<String> roles = Arrays.asList("cantante", "guitarra", "guitarra", "bateria");
		Cancion cancion = new Cancion("Tema 3", new ArrayList<>(roles));

		Artista cantante = new Artista("Voz", Arrays.asList("cantante"), new ArrayList<Banda>());
		Artista guitarrista = new Artista("Guitarra", Arrays.asList("guitarra"), new ArrayList<Banda>());

		cancion.contratarArtista(cantante, "cantante");
		cancion.contratarArtista(guitarrista, "guitarra");

		Map<String, Integer> faltantes = cancion.rolesFaltantesConCantidad();

		assertEquals(3, faltantes.size(), "Deben existir 3 roles distintos");
		assertEquals(0, faltantes.get("cantante"), "El rol cantante ya esta cubierto");
		assertEquals(1, faltantes.get("guitarra"), "Habia 2 guitarra y solo 1 contrato");
		assertEquals(1, faltantes.get("bateria"), "No se contrato ningun bateria");
	}

	@Test
	public void tieneTodosLosRolesCubiertosFalseCuandoFaltaAlguno() {
		Artista cantante = new Artista("Juan", Arrays.asList("cantante"), new ArrayList<Banda>());
		cancionBase.contratarArtista(cantante, "cantante");

		assertFalse(cancionBase.tieneTodosLosRolesCubiertos(), "No deberia estar completa porque faltan roles");
	}

	@Test
	public void tieneTodosLosRolesCubiertosTrueCuandoNoFaltaNinguno() {
		List<String> roles = Arrays.asList("cantante", "guitarra");
		Cancion cancion = new Cancion("Tema Completo", new ArrayList<>(roles));

		Artista cantante = new Artista("Voz", Arrays.asList("cantante"), new ArrayList<Banda>());
		Artista guitarrista = new Artista("Guitarra", Arrays.asList("guitarra"), new ArrayList<Banda>());

		assertTrue(cancion.contratarArtista(cantante, "cantante"));
		assertTrue(cancion.contratarArtista(guitarrista, "guitarra"));

		assertTrue(cancion.tieneTodosLosRolesCubiertos(), "Todos los roles deberian estar cubiertos");
	}

	@Test
	public void calcularCostoSumaCostosDeLosContratos() {
		List<String> roles = Arrays.asList("cantante", "guitarra");
		Cancion cancion = new Cancion("Tema Costo", new ArrayList<>(roles));

		Artista cantante = new ArtistaInvitado("Voz", Arrays.asList("cantante"), new ArrayList<Banda>(), 1000, 2);
		Artista guitarrista = new ArtistaInvitado("Guitarra", Arrays.asList("guitarra"), new ArrayList<Banda>(), 2000,
				2);

		cancion.contratarArtista(cantante, "cantante");
		cancion.contratarArtista(guitarrista, "guitarra");

		assertEquals(3000.0, cancion.calcularCosto(), "El costo total debe ser la suma de los costos base");
	}

	@Test
	public void quitarContratosDeEliminaSoloContratosDelArtista() {
		List<String> roles = Arrays.asList("cantante", "guitarra");
		Cancion cancion = new Cancion("Tema 4", new ArrayList<>(roles));

		Artista cantante = new Artista("Voz", Arrays.asList("cantante"), new ArrayList<Banda>());
		Artista guitarrista = new Artista("Guitarra", Arrays.asList("guitarra"), new ArrayList<Banda>());

		cancion.contratarArtista(cantante, "cantante");
		cancion.contratarArtista(guitarrista, "guitarra");

		assertEquals(2, cancion.getContratos().size());

		cancion.quitarContratosDe(cantante);

		for (Contrato_x_Cancion c : cancion.getContratos()) {
			assertNotEquals(cantante, c.getArtista(), "No debe quedar ningun contrato del artista eliminado");
		}
		assertEquals(1, cancion.getContratos().size(), "Debe quedar solo el contrato del otro artista");
	}

}
