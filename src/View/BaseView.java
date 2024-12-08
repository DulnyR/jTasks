package View;

import Controller.Controller;

public abstract class BaseView {
    protected Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public abstract void init();
    public abstract void showMessage(String string);
    public abstract void showErrorMessage(String string);
    public abstract void end();

    // Imports and exports in BaseView as they will be the same no matter the view
    // Protected so that these can be accessed by subclasses
    protected void exportJSON() {
        controller.setJSONExporter();
        export();
    }

    protected void exportCSV() {
        controller.setCSVExporter();
        export();
    }

    // Used to check if tasks are not empty for export
    protected void export() {
        if (controller.exportTasks()) {
            System.out.println("Tasks have been exported successfully. You can find them in your home directory.");
        } else {
            System.out.println("No tasks to export. Add new tasks to proceed");
        }
    }

    protected void importJSON() {
        controller.setJSONExporter();
        importFile();
    }

    protected void importCSV() {
        controller.setCSVExporter(); 
        importFile(); 
    }

    protected void importFile() {
        if (controller.importFile()) {
            System.out.println("Tasks have been imported successfully.");
        } else {
            System.out.println("File does not exist/is empty. Make sure the task is located " +
                                "in your home directory. No tasks were imported");
        }
    }
}
