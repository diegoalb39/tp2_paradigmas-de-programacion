package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import artistas.Artista;
import recitales.Cancion;
import recitales.Contrato_x_Cancion;

class Contrato_x_CancionTest {

	@Test
	void constructorSeteaCostoCorrectamente() {
		Artista artista = new Artista("Juan", Arrays.asList("cantante"), new ArrayList<>());

		Cancion cancion = new Cancion("Tema 1", new ArrayList<>(Arrays.asList("cantante")));

		Contrato_x_Cancion contrato = new Contrato_x_Cancion(artista, cancion, "cantante");

		assertSame(artista, contrato.getArtista());
		assertSame(cancion, contrato.getCancion());
		assertEquals("cantante", contrato.getRol());
		assertEquals(0.0, contrato.getCosto());
	}

}
