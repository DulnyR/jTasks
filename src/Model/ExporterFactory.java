package Model;

public class ExporterFactory {
    public static IExporter getExporter(String type) {
        switch(type) {
            case "csv":
                return new CSVExporter();
            case "json":
                return new JSONExporter();
            default:
                return null;
        }
    }
}
