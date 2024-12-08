package View;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import com.coti.tools.Esdia;

import Model.Task;

public class InteractiveView extends BaseView {

    public void init() {
        System.out.println("Welcome to the Interactive View of jTasks!\n");

        mainMenu();
    }
    
    private void mainMenu() {
        int option;
        do {
            option = Esdia.readInt("\nSelect one of the following options: \n" +
                "1. Add new task\n" + 
                "2. List due tasks\n" +
                "3. List task history\n" +
                "4. Export to JSON\n" +
                "5. Export to CSV\n" +
                "6. Import from JSON\n" +
                "7. Import from CSV\n" +
                "8. Exit\n\n" +
                "Selection: ", 1, 8);
            List<Task> tasks;
            switch(option) {
                case 1: 
                    addNewTask();
                    break;
                // Only execute the listing of tasks and selection process if due tasks exist
                case 2:
                    tasks = listDueTasks();
                    if (tasks != null) selectTask(tasks);
                    break;
                // As above but with task history
                case 3:
                    tasks = listTaskHistory();
                    if (tasks != null) selectTask(tasks);
                    break;
                case 4: 
                    exportJSON();
                    break;
                case 5:
                    exportCSV();
                    break;
                case 6:
                    importJSON();
                    break;
                case 7:
                    importCSV();
                    break;
                case 8:
                    end();
                default:
            }
        } while (option != 8);
    }

    // User is asked for details need to create new task
    private void addNewTask() {
        String title = Esdia.readString("\nEnter the name of the task: ");
        String content = Esdia.readString("Describe the task: ");
        int priority = Esdia.readInt("What's the priority of the task (1 (Low) - 5 (High))?", 1, 5);
        int estimatedDuration = Esdia.readInt("How long do you think this task will take to complete (minutes)? ");
        controller.addNewTask(title, content, priority, estimatedDuration);
    }

    // Checks if due tasks exist and prints corresponding message
    private List<Task> listDueTasks() {
        List<Task> tasks = controller.getDueTasks();
        if (tasks != null) {
            String taskHistory = "Due tasks (Ranked by Priority): \n";
            for (int i = 0; i < tasks.size(); i++) {
                taskHistory += taskPriorityString(i, tasks.get(i));
            }
            System.out.println(taskHistory);
        }
        else {
            System.out.println("No tasks have been added yet. Add a new one to proceed.");
        }
        return tasks;
    }

    // As above but with task history
    private List<Task> listTaskHistory() {
        List<Task> tasks = controller.getTaskHistory();
        if (tasks != null) {
            String taskHistory = "All tasks (Sorted by Date): \n";
            for (int i = 0; i < tasks.size(); i++) {
                taskHistory += taskDateString(i, tasks.get(i));
            }
            System.out.println(taskHistory);
        }
        else {
            System.out.println("No tasks have been added yet. Add a new one to proceed.");
        }
        return tasks;
    }

    private String taskPriorityString(int id, Task task) {
        return (id + 1) + ". " + task.getTitle() + " (" + task.getPriority() + ")\n";
    }

    private String taskDateString(int id, Task task) {
        return (id + 1) + ". " + task.getTitle() + " (" + task.getDateString() + ")\n";
    }

    // Detail and edit view
    private void selectTask(List<Task> tasks) {
        int position;
        String message = "Select task by number to edit/view task details or press 0 to go back: ";
        do {
            position = Esdia.readInt(message);
            message = "Invalid position, revise the list above and try again: ";
            if(position == 0) return;
        } while(position > tasks.size());

        String identifier = tasks.get(position - 1).getIdentifier();

        int option;
        do {
            Task task = controller.getTask(identifier);
            displayTaskDetails(task);

            option = Esdia.readInt("Select one of the following options: \n" +
                "1. Mark as " + (task.isCompleted() ? "uncompleted" : "completed") + "\n" +
                "2. Edit title\n" +
                "3. Edit date\n" +
                "4. Edit description\n" +
                "5. Edit priority\n" +
                "6. Edit estimated duration\n" +
                "7. Delete task\n" +
                "8. Return to main menu\n" +
                "Selection: ",
            1, 8);
            
            handleTaskOption(option, identifier);
        } while(option < 7);
    }

    private void displayTaskDetails(Task task) {
        System.out.println( 
            "\nTask Details:\n" + 
            "Task ID: " + task.getIdentifier() + "\n" +
            "Title: " + task.getTitle() + "\n" +
            "Date Added: " + task.getDateString() + "\n" +
            "Description: " + task.getContent() + "\n" +
            "Priority: " + task.getPriority() + "\n" +
            "Estimated Duration: " + task.getEstimatedDuration() + " minutes\n" +
            "Completed: " + (task.isCompleted() ? "Yes" : "No") + "\n"
        );
    }

    private void handleTaskOption(int option, String identifier) {
        switch (option) {
            case 1:
                controller.toggleComplete(identifier);
                break;
            case 2:
                String title = Esdia.readString("Enter new title: ");
                controller.setTitle(identifier, title);
                break;
            case 3:
                Date date = askDate();
                controller.setDate(identifier, date);
                break;
            case 4:
                String content = Esdia.readString("Enter new description: ");
                controller.setContent(identifier, content);
                break;
            case 5:
                int priority = Esdia.readInt("Enter new priority (1 (Low) - 5 (High)): ", 1, 5);
                controller.setPriority(identifier, priority);
                break;
            case 6:
                int duration = Esdia.readInt("Enter new estimated duration (minutes): ");
                controller.setEstimatedDuration(identifier, duration);
                break;
            case 7:
                controller.removeTask(identifier);
                break;
            case 8:
                mainMenu();
                break;
            default:
                break;
        }
    }

    private Date askDate() {
        int year = Esdia.readInt("Enter year of new date (e.g. 2024): ");
        int month = Esdia.readInt("Enter month of new date: ", 1, 12);
        int day;
        switch (month) {
            case 2:
                day = Esdia.readInt("Enter day of new date: ", 1, (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) ? 29 : 28);
                break;
            case 4: case 6: case 9: case 11:
                day = Esdia.readInt("Enter day of new date: ", 1, 30);
                break;
            default:
                day = Esdia.readInt("Enter day of new date: ", 1, 31);
                break;
        }
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public void showMessage(String string) {
        System.out.println(string + "\n");
    }

    public void showErrorMessage(String string) {
        System.err.println(string + "\n");
    }

    public void end() {
        controller.end();
    }
}
