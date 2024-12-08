package Model;

public class RepositoryException extends Exception {
    // Exception designed to make repository error more recogniseable
    public RepositoryException(String message) {
        super("Error when using repository. Details: " + message);
    }
}
