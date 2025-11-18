package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import artistas.Artista;
import recitales.Banda;
import recitales.Cancion;
import recitales.Contrato_x_Cancion;

class CancionTest {

	// Helper rápido para crear un Artista con un rol
    private Artista crearArtistaSimple(String nombre, String rol, double costoBase) {
        List<String> roles = new ArrayList<>();
        roles.add(rol);

        List<Banda> bandas = new ArrayList<>(); // vacío por ahora
        Artista artista = new Artista(nombre, roles, bandas);
        artista.setCostoBase(costoBase); // si tenés setter; si no, ajustá al constructor correcto
        //artista.setDisponibilidad(true);
        return artista;
    }

    @Test
    public void contratarArtista_deberiaAgregarContratoCuandoTodoEsValido() {
        Artista artista = crearArtistaSimple("Brian May", "guitarra eléctrica", 100.0);
        List<String> rolesCancion = Arrays.asList("guitarra eléctrica");

        Cancion cancion = new Cancion("Somebody to Love", rolesCancion);

        boolean resultado = cancion.contratarArtista(artista, "guitarra eléctrica");

        assertTrue(resultado);
        assertEquals(1, cancion.getContratos().size());
        Contrato_x_Cancion contrato = cancion.getContratos().get(0);
        assertSame(artista, contrato.getArtista());
        assertEquals("guitarra eléctrica", contrato.getRol());
        assertEquals(100.0, contrato.getCosto());
        assertEquals(1, artista.getContratos().size());
    }

}
