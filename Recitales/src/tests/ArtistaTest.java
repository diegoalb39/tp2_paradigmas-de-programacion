package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import artistas.Artista;
import recitales.Banda;
import recitales.Cancion;
import recitales.Contrato_x_Cancion;

class ArtistaTest {
	
	private Artista artista;
	private List<String> roles;
	private Cancion cancion;
	
	@BeforeEach
	public void setUp() {
		roles = new ArrayList<>(Arrays.asList("cantante", "guitarra"));
        List<Banda> bandas = new ArrayList<>();

        artista = new Artista("Juan", roles, bandas);
        cancion = new Cancion("cancion", roles);
	}
	
	@Test
    public void contieneRolTrue() {
        assertTrue(artista.contieneRol("cantante"),"Deberia contener el rol cantante");
    }
	
	@Test
	public void contieneRolFalse() {
		assertFalse(artista.contieneRol("bateria"),"No deberia contener el rol bateria");
	}
	
	@Test
	public void tieneContratosFalseCuandoNoHayContratos() {
		assertFalse(artista.tieneContratos(), "No deberia tener contratos");
	    assertEquals(0, artista.getContratos().size());
	}
	
	@Test
    void agregarContratoYTieneContratosTrue() {
        Contrato_x_Cancion contrato = new Contrato_x_Cancion(artista, cancion, "cantante");

        artista.agregarContrato(contrato);
        assertEquals(1, artista.getContratos().size(), "Deberia tener un contrato");
        assertSame(contrato, artista.getContratos().get(0),"El contrato agregado deberia ser el mismo");
        assertTrue(artista.tieneContratos(),"Deberia tener contratos");
    }
	
	@Test
    public void testEsBaseTrueParaArtistaComun() {
        assertTrue(artista.esBase(),"Un Artista comun deberia ser base por defecto");
    }

    @Test
    public void testGetDisponibilidadTrueParaArtistaBase() {
        assertTrue(artista.getDisponibilidad(), "Artista base siempre deberia estar disponible, ya que no tiene max de canciones");
    }
}
