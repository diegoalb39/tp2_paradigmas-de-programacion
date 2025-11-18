package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import artistas.Artista;
import recitales.Banda;

class BandaTest {

	@Test
    void constructorYGetters() {
        List<Artista> integrantes = new ArrayList<>(
                Arrays.asList(
                        new Artista("Juan", Arrays.asList("cantante"), new ArrayList<>()),
                        new Artista("Ana", Arrays.asList("guitarra"), new ArrayList<>())
                )
        );

        Banda banda = new Banda("Los Pibes del Rock", integrantes);

        assertEquals("Los Pibes del Rock", banda.getNombre());
        assertNotNull(banda.getIntegrantes());
        assertEquals(2, banda.getIntegrantes().size());
        assertEquals("Juan", banda.getIntegrantes().get(0).getNombre());
    }

    @Test
    void listaIntegrantesEsLaMismaReferenciaQueLaPasada() {
        List<Artista> integrantes = new ArrayList<>();
        Banda banda = new Banda("Banda X", integrantes);

        assertSame(integrantes, banda.getIntegrantes(),"La referencia deberia ser la misma");
    }

}
