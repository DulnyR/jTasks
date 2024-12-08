package Model;

import java.util.List;

public interface IRepository {

    public Task addTask(Task t) throws RepositoryException;
    public Task getTask(String identifier) throws RepositoryException;
    public void removeTask(String identifier) throws RepositoryException;
    public boolean taskExists(String identifier) throws RepositoryException;
    public List<Task> getAllTasks() throws RepositoryException;
    public void modifyTask(Task t) throws RepositoryException;

}
