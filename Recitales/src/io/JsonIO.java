package io;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
public class JsonIO {
	private static final ObjectMapper mapper = new ObjectMapper();

	private JsonIO() {
	}

	public static List<ArtistaJson> cargarArtistas(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<List<ArtistaJson>>() {
		});
	}

	public static List<CancionJson> cargarCanciones(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<List<CancionJson>>() {
		});
	}

	public static List<String> cargarArtistasBase(Path path) throws IOException {
		String json = Files.readString(path, StandardCharsets.UTF_8);
		return mapper.readValue(json, new TypeReference<List<String>>() {
		});
	}

	//print para exportar resultados, TODO testear
	public static <T> void guardar(Path path, T objeto) throws IOException {
		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(objeto);
		Files.writeString(path, json, StandardCharsets.UTF_8);
	}
}
