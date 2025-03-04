package dependencies.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Reads a JSON file and deserializes it into an object of the specified type.
     *
     * @param <T>  The type of the object to deserialize.
     * @param file The file to read from.
     * @param type The type reference for deserialization.
     * @return The deserialized object.
     * @throws IOException If an I/O error occurs.
     */
    public static <T> T readFromFile(File file, TypeReference<T> type) throws IOException {
        if (!file.exists()) {
            throw new IOException("File " + file.getName() + " does not exist.");
        }

        return objectMapper.readValue(file, type);
    }

    /**
     * Writes an object to a JSON file with pretty printing.
     *
     * @param file   The file to write to.
     * @param object The object to serialize.
     * @throws IOException If an I/O error occurs.
     */
    public static void writeToFile(File file, Object object) throws IOException {
        objectMapper.writeValue(file, object);
    }
}
