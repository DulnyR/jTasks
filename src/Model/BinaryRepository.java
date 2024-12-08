package Model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BinaryRepository implements IRepository, Serializable  {

    // HashMap for O(1) access time
    private Map<String, Task> tasks;

    public BinaryRepository() {
        tasks = new HashMap<String, Task>();
    }
   
    public Task addTask(Task task) {
        tasks.put(task.getIdentifier(), task);
        return task;
    }

    public Task getTask(String identifier) {
        return tasks.get(identifier);
    }

    public void removeTask(String identifier) {
        tasks.remove(identifier);
    }

    public boolean taskExists(String identifier) {
        return tasks.containsKey(identifier);
    }

    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    // Seeing as only one identifier is allowed per HashMap we can add it using the addTask function
    public void modifyTask(Task t) {
        addTask(t);
    }
}
