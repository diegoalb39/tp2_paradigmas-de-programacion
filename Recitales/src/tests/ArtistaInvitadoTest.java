package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import artistas.Artista;
import artistas.ArtistaInvitado;
import recitales.Banda;
import recitales.Contrato_x_Cancion;

class ArtistaInvitadoTest {

	private List<String> rolesBase;

	@BeforeEach
	public void setUp() {
		rolesBase = new ArrayList<>();
		rolesBase.add("cantante");
	}

	@Test
	public void esBaseFalseParaArtistaInvitado() {
		List<Banda> bandas = new ArrayList<>();
		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandas, 1000.0, 3);

		assertFalse(invitado.esBase(), "Un ArtistaInvitado no deberia ser base");
	}

	@Test
	public void entrenarExitosoSinContratos() {
		List<Banda> bandas = new ArrayList<>();
		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandas, 1000.0, 3);

		boolean resultado = invitado.entrenar("guitarra");

		assertTrue(resultado, "Deberia poder entrenar cuando no tiene contratos y no es base");
		assertTrue(invitado.getRoles().contains("guitarra"), "Deberia agregar el nuevo rol a la lista de roles");
		assertEquals(1500.0, invitado.getCostoBase(),
				"Despues de entrenar, el costo base deberia multiplicarse por 1.5");
	}

	@Test
	public void entrenarFallaSiYaTieneContratos() {
		List<Banda> bandas = new ArrayList<>();
		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandas, 1000.0, 3);
		Contrato_x_Cancion contrato = new Contrato_x_Cancion(invitado, null, "cantante");
		invitado.agregarContrato(contrato);

		boolean resultado = invitado.entrenar("guitarra");

		assertFalse(resultado, "No deberia poder entrenar si ya tiene contratos");
		assertFalse(invitado.getRoles().contains("guitarra"), "No deberia haberse agregado el nuevo rol");
		assertEquals(1000.0, invitado.getCostoBase(), "El costo base deberia seguir igual");
	}

	@Test
	public void tieneDescuentoTrueCuandoComparteBandaConArtistaBase() {
		Artista base = new Artista("Titular", Arrays.asList("cantante"), new ArrayList<Banda>());

		List<Artista> integrantes = new ArrayList<>();
		integrantes.add(base);

		Banda banda = new Banda("Banda Principal", integrantes);

		List<Banda> bandasInvitado = new ArrayList<>();
		bandasInvitado.add(banda);

		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandasInvitado, 1000.0,
				3);

		assertTrue(invitado.tieneDescuento(), "Deberia tener descuento si en alguna banda hay un artista base");
		assertEquals(500.0, invitado.getCostoBase(), "Con descuento deberia cobrar el 50% del costo base");
	}

	@Test
	public void tieneDescuentoFalseCuandoNoHayArtistaBaseEnBandas() {
		ArtistaInvitado otroInvitado = new ArtistaInvitado("OtroInvitado", new ArrayList<>(rolesBase),
				new ArrayList<Banda>(), 800.0, 2);

		List<Artista> integrantes = new ArrayList<>();
		integrantes.add(otroInvitado);

		Banda bandaSoloInvitados = new Banda("Banda Invitados", integrantes);

		List<Banda> bandasInvitado = new ArrayList<>();
		bandasInvitado.add(bandaSoloInvitados);

		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandasInvitado, 1000.0,
				3);

		assertFalse(invitado.tieneDescuento(), "No deberia tener descuento si en las bandas no hay artistas base");
		assertEquals(1000.0, invitado.getCostoBase(), "Sin descuento, el costo base deberia mantenerse igual");
	}

	@Test
	public void getDisponibilidadSegunMaxCancionesYContratos() {
		List<Banda> bandas = new ArrayList<>();
		ArtistaInvitado invitado = new ArtistaInvitado("Invitado", new ArrayList<>(rolesBase), bandas, 1000.0, 2);

		assertTrue(invitado.getDisponibilidad(), "Con 0 contratos y maxCanciones=2 deberia estar disponible");

		Contrato_x_Cancion c1 = new Contrato_x_Cancion(invitado, null, "cantante");
		invitado.agregarContrato(c1);
		assertTrue(invitado.getDisponibilidad(), "Con 1 contrato y maxCanciones=2 deberia estar disponible");

		Contrato_x_Cancion c2 = new Contrato_x_Cancion(invitado, null, "cantante");
		invitado.agregarContrato(c2);
		assertFalse(invitado.getDisponibilidad(), "Con 2 contratos y maxCanciones=2 no deberia estar disponible");
	}

}
