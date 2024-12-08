package Model;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CSVExporter implements IExporter {

    // Final path to ensure path remains the same throughout
    private static final Path path = Paths.get(System.getProperty("user.home"), "ouput.csv");

    public void exportTasks(List<Task> tasks) throws ExporterException {
        ArrayList<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            lines.add(task.getTaskAsDelimitedString(","));
        }
        try {
            Files.write(path, lines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new ExporterException(e.getMessage());
        }
    }

    public List<Task> importTasks() throws ExporterException {
        List<Task> tasks = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(path);
            for (String line : lines) {
                Task task = Task.getTaskFromDelimitedString(line, ",");
                if(task != null) {
                    tasks.add(task);
                }
            }
        } catch (Exception e) {
            throw new ExporterException(e.getMessage());
        }

        return tasks;
    }
}
