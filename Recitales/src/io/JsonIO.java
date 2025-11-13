package io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import artistas.Artista;
import recitales.Cancion;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
//import java.util.HashSet;
import java.util.List;
import java.util.Set;
public class JsonIO {
	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonIO() {
	}

	public static List<Artista> cargarArtistas(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<List<Artista>>() {
		});
	}

	public static List<Cancion> cargarCanciones(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<List<Cancion>>() {
		});
	}

	public static Set<String> cargarArtistasBase(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<Set<String>>() {
		});
	}

	//print para exportar resultados
	public static <T> void guardar(Path path, T objeto) throws IOException {
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objeto);
		Files.writeString(path, json, StandardCharsets.UTF_8);
	}
}
