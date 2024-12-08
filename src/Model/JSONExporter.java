package Model;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;

public class JSONExporter implements IExporter {

    // Final path to ensure path remains the same throughout
    private static final Path path = Paths.get(System.getProperty("user.home"), "output.json");

    public void exportTasks(List<Task> tasks) throws ExporterException {
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        try {
            Files.write(path, json.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new ExporterException(e.getMessage());
        }
    }

    public List<Task> importTasks() throws ExporterException {
        Gson gson = new Gson();
        try {
            String json = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            Type listType = new TypeToken<List<Task>>() {}.getType();
            return gson.fromJson(json, listType);
        } catch (Exception e) {
            throw new ExporterException(e.getMessage());
        }
    }
}
