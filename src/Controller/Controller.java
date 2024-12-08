package Controller;

import View.BaseView;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import Model.ExporterException;
import Model.Model;
import Model.RepositoryException;
import Model.Task;

public class Controller {
    private Model model;
    private BaseView view;

    public Controller(Model model, BaseView view) {
        this.model = model;
        this.view = view;
    }

    public void start() {
        view.init();
    }

    // Tries to save data when program is closed and lets user know of outcome
    public void end() {
        try {
            model.saveData();
            view.showMessage("Tasks have been saved successfully.");
        } catch (IOException e) {
            view.showErrorMessage("Tasks could not be saved. Details: " + e);
        }
    }

    // Tries to add new task in model and lets user know of outcome
    public void addNewTask(String title, String content, int priority, int estimatedDuration) {
        try {
            model.addNewTask(title, content, priority, estimatedDuration);
            view.showMessage("Task uploaded successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    // Tries to list due tasks and lets user know of outcome
    public List<Task> getDueTasks() {
        try {
            return model.getDueTasks();
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return null;
        }
    }

    // Tries to list task history and lets user know of outcome
    public List<Task> getTaskHistory() {
        try {
            return model.getTaskHistory();
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return null;
        }
    }

    //Tries to load date and lets user know of outcome
    public void loadData() {
        try {
            model.loadData();
            view.showMessage("Previous tasks have been loaded successfully.");
        } catch (Exception e) {
            view.showMessage("No previous tasks found.");
        }
    }

    // Tries to check if task exists and lets user know of outcome
    public boolean taskExists(String identifier) {
        try {
            return model.taskExists(identifier);
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return false;
        }
    }

    public Task getTask(String identifier) {
        try {
            return model.getTask(identifier);
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return null;
        }
    }

    // Tries to check if task is complete and lets user know of outcome
    public boolean isTaskComplete(String identifier) {
        try {
            return model.isTaskComplete(identifier);
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return false;
        }
    }

    // Tries to toggle complete and lets user know of outcome
    public void toggleComplete(String identifier) {
        try {
            model.toggleComplete(identifier);
            view.showMessage("Completion status changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    // Functions that set/change specific attributes of task and let user know of outcome
    public void setTitle(String identifier, String title) {
        try {
            model.setTitle(identifier, title);
            view.showMessage("Title changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    public void setDate(String identifier, Date date) {
        try {
            model.setDate(identifier, date);
            view.showMessage("Date changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    public void setContent(String identifier, String content) {
        try {
            model.setContent(identifier, content);
            view.showMessage("Description changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    public void setPriority(String identifier, int priority) {
        try {
            model.setPriority(identifier, priority);
            view.showMessage("Priority changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    public void setEstimatedDuration(String identifier, int duration) {
        try {
            model.setEstimatedDuration(identifier, duration);
            view.showMessage("Estimated duration changed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    // Removes task and lets user know of outcome
    public void removeTask(String identifier) {
        try {
            String title = model.getTask(identifier).getTitle();
            model.removeTask(identifier);
            view.showMessage("Task " + title + " with ID " + identifier + " has been removed successfully.");
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
        }
    }

    // Exports tasks, lets user know if task are empty with boolean
    public boolean exportTasks() {
        boolean success = false;
        try {
            success = model.exportTasks();
        } catch (RepositoryException | ExporterException e) {
            view.showErrorMessage(e.getMessage());
        }
        return success;
    }

    public void setCSVExporter() {
        model.setCSVExporter();
    }

    public void setJSONExporter() {
        model.setJSONExporter();
    }

    // Imports tasks, let user know if imported tasks are empty or null
    public boolean importFile() {
        try {
            return model.importFile();
        } catch (ExporterException | RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return false;
        } 
    }

    public boolean setNotionRepository(String apiKey, String databaseId) {
        try {
            model.setNotionRepository(apiKey, databaseId);
            return true;
        } catch (RepositoryException e) {
            view.showErrorMessage(e.getMessage());
            return false;
        }
    }
}
