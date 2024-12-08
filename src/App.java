import Model.BinaryRepository;
import Model.Model;
import View.BaseView;
import View.InteractiveView;
import Controller.Controller;

public class App {
    public static void main(String[] args) throws Exception {
        // Set to Binary Repository by default
        Model model = new Model(new BinaryRepository());
        BaseView view = new InteractiveView();
        Controller controller = new Controller(model, view);

        view.setController(controller);

        // Checks for arguments in order to check if notion repo should be used 
        // Binary is used by default in constructor
        boolean notion = false;
        if (args.length > 0 && args[0].equals("--repository")) {
            if (args.length > 1 && args[1].equals("notion")) {
                if (args.length == 4) {
                    String apiKey = args[2];
                    String databaseId = args[3];
                    // Set to Notion Repository if requested
                    // Check if APIKey and Database ID is correct and connect to Binary Repository if not
                    view.showMessage("Attempting Notion connection...");
                    notion = controller.setNotionRepository(apiKey, databaseId);
                }
            }
        }
        if (notion) {
            view.showMessage("Successfully connected to the Notion Repository.");
        } else {
            view.showMessage("Loading tasks from the Binary Repository...");
            controller.loadData();
        }

        controller.start();
    }
}
