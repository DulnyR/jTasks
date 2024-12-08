package Model;

import java.util.List;

public interface IExporter {

    public void exportTasks(List<Task> tasks) throws ExporterException;
    public List<Task> importTasks() throws ExporterException;

}
