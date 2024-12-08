package Model;

public class ExporterException extends Exception {
    // Exception for unsuccessful export/import
    public ExporterException(String message) {
        super("Error when exporting/importing. Details: " + message);
    }
}
