package Model;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Model {
    private IRepository iRepository;
    private IExporter iExporter;

    public Model(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    public void setCSVExporter() {
        this.iExporter = ExporterFactory.getExporter("csv");
    }

    public void setJSONExporter() {
        this.iExporter = ExporterFactory.getExporter("json");
    }

    public void addNewTask (String title, String content, int priority, int estimatedDuration) throws RepositoryException {
        Task task = new Task(title, content, priority, estimatedDuration);
        // Check if task with same id already exists
        if (!iRepository.taskExists(task.getIdentifier())) {
            iRepository.addTask(task);
        } else {
            throw new RepositoryException("Task with ID " + task.getIdentifier() + " already exists.");
        }    
    }

    public List<Task> getDueTasks() throws RepositoryException {
        List<Task> tasks = iRepository.getAllTasks();
        Collections.sort(tasks);
        ArrayList<Task> dueTasks = new ArrayList<>();
        for (Task task : tasks) {
            if(!task.isCompleted()) {
                dueTasks.add(task);
            }
        }
        if(dueTasks.isEmpty()) return null;
        return dueTasks;
    }

    public List<Task> getTaskHistory() throws RepositoryException {
        List<Task> tasks = iRepository.getAllTasks();
        if (tasks.isEmpty()) return null;
        Collections.sort(tasks, new DateComparator());
        return tasks;
    }

    public boolean taskExists(String identifier) throws RepositoryException {
        return iRepository.taskExists(identifier);
    }

    public boolean isTaskComplete(String identifier) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        return task.isCompleted();
    }

    public void toggleComplete(String identifier) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        task.setCompleted(!task.isCompleted());
        iRepository.modifyTask(task);
    }

    public void setTitle(String identifier, String title) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        task.setTitle(title);
        iRepository.modifyTask(task);
    }

    public void setDate(String identifier, Date date) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        task.setDate(date);
        iRepository.modifyTask(task);
    }

    public void setContent(String identifier, String content) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        task.setContent(content);
        iRepository.modifyTask(task);
    }

    public void setPriority(String identifier, int priority) throws RepositoryException {
        Task task = iRepository.getTask(identifier);
        task.setPriority(priority);
        iRepository.modifyTask(task);
    }

    public void setEstimatedDuration(String identifier, int duration) throws RepositoryException {
       Task task = iRepository.getTask(identifier);
       task.setEstimatedDuration(duration);
       iRepository.modifyTask(task);
    }

    public void removeTask(String identifier) throws RepositoryException {
        iRepository.removeTask(identifier);
    }

    // Load data from binary file
    public void loadData() throws Exception {
        try (FileInputStream fileInputStream = new FileInputStream("repository.bin")) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            IRepository iRepository = (IRepository) objectInputStream.readObject();
            this.iRepository = iRepository;
            objectInputStream.close();
        } catch (ClassNotFoundException | IOException e) {
            throw e;
        }
    }

    // Save date to binary file if required
    public void saveData() throws IOException {
        if(iRepository instanceof BinaryRepository) {
            try (FileOutputStream fileOutputStream = new FileOutputStream("repository.bin")) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(iRepository);
                objectOutputStream.close();
            } catch (IOException e) {
                throw e;
            }
        }
    }

    // Export task while sorting by date
	public boolean exportTasks() throws RepositoryException, ExporterException {
        List<Task> tasks = iRepository.getAllTasks();
        if (tasks.isEmpty()) return false;
        Collections.sort(tasks, new DateComparator());
		iExporter.exportTasks(tasks);
        return true;
	}

    // Import file while checking for duplicate
    public boolean importFile() throws ExporterException, RepositoryException {
        List<Task> tasks = iExporter.importTasks();
        if (tasks == null || tasks.isEmpty()) return false;
        for (Task task : tasks) {
            if(!iRepository.taskExists(task.getIdentifier())){
                iRepository.addTask(task);
            }
        }
        return true;
    }

    public void setNotionRepository(String apiKey, String databaseId) throws RepositoryException {
        this.iRepository = new NotionRepository(apiKey, databaseId);
    }

    public Task getTask(String identifier) throws RepositoryException {
        return iRepository.getTask(identifier);
    }
}
