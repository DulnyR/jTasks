Design Choices
Storing tasks as a HashMap in the Binary Repository - Storing tasks as a hashmap will allow for O(1) search time when searching by the unique identifier.
Modify Task uses Add Task in Binary Repository - Modify task uses add task as it replaces the corresponding identifier entry in the HashMap of tasks.
Using static final path in Exporters - This will ensure that the path will remain the same whether the app is exporting or importing.
Exporting as a Boolean - Exporting as a boolean notifies the user if there are no tasks to export.
Imports and Exports in BaseView - Import and Export views are implemented in BaseView as they will be the same no matter what the view is.
Task Sorting - Tasks are sorted by priority by default but a Date Comparator is available for actions such as exporting and task history.
YYYY-MM-DD Date Format throughout the program - Using the same date format in the entire program minimizes the chance of errors during conversion.
Binary Repository is used if API Key/Database ID is incorrect - The user is informed that the connection has been unsuccessful and that the Binary Repository is used.

User Interface (Interactive View)
The Interactive View UI has been implemented in a way that gives the user the ability to navigate through the program with ease and access all of the available options that it provides. The controller has been set up in a way that allows for easy implementation of new views. 

CRUD Operations
All CRUD operations are handled using a try catch statement that throws a Repository Exception with a description in the case of an error. The user is also updated if their changes have been executed successfully. 

🟢 Add New Task
Users are asked for title, content, priority and estimated duration which is the minimum amount of information required to create a new Task. The ID of a Task is assigned to it during its creation in the constructor with a random UUID. Existence of a Task with the same ID is checked in the model before adding to the corresponding repository.

🟣 List Due Tasks
A list of tasks that have not been completed is fetched from the repository through the controller and model. These are ranked by priority using the comparable property of the Task class. The user is informed whether the fetch was successful and the list empty. 

🟣 List Task History
Similar to above but this time all of the tasks are shown and are sorted by date instead of priority using the Date Comparator class.

🟣 Select Task
After either of the lists are presented the user can choose the task that they would like to view/edit by using the position in the list or exiting back to the main menu by pressing ‘0’. In the case that a Task is chosen the task details are displayed and editing options are given. The options below are shown until the user either returns to the main menu or deletes the Task.

🔵 Mark as Completed/Uncompleted
This option is presented giving the user the ability to toggle between completed and uncompleted based on the current state of the completed boolean. 

🔵 Edit Title
🔵 Edit Description
🔵 Edit Priority
🔵 Edit Estimated Duration
All of these options give the user to change the corresponding variable.

🔵 Edit Date
This asks the user for a year, month and date and converts it to a Date object that can be assigned to the Task.

🔴 Delete Task
This option allows the user to remove the task that is currently selected. 
Export/Import (CSV/JSON)
The view functionality of these options is placed in the Base View class as it will be equal among all views. All operations in this section throw Exporter Exception in the case of any errors and let the user know what the problem is.

Export CSV
The exporter of the program is set to the CSV Exporter using the Exporter Factory. Tasks are exported to the home directory of the user with the name output.csv. 

Import CSV
The exporter of the program is set to the CSV Exporter using the Exporter Factory. Tasks are imported from the file output.csv in the home directory of the user. The user is informed if this file does not exist.

Export JSON
The exporter of the program is set to the JSON Exporter using the Exporter Factory. Tasks are exported to the home directory of the user with the name output.json. 

Import JSON
The exporter of the program is set to the JSON Exporter using the Exporter Factory. Tasks are imported from the file output.json in the home directory of the user. The user is informed if this file does not exist.

Task Object
The task object has all the variables required as well as a Serial Version UID in order to be serialisable. Tasks are identified using a UUID identifier that is generated at creation. Task is Comparable and sorted by priority by default. 

Persistence/Repositories
The repository can be set by the user using command line arguments when launching the application, which are specified below (Binary Repository by default). Persistence varies based on the repository chosen by the user.

Binary Repository
In the case of a Binary Repository, it can be serialised in a .bin file. Thanks to the repository being serialisable, the model can save data that can then be loaded into the program at startup the next time a user decides to use a Binary Repository.

java -jar app.jar --repository bin 

Notion Repository
In the case of a Notion Repository, the data can be uploaded to Notion with every operation and will be available to use whenever the user next uses the application and decides to use the Notion Repository. In order to connect, the API Key and Database ID have to be specified by the user in the command line arguments.

 java -jar app.jar --repository notion "API_KEY" "DATABASE_ID" 

Robustness
All of the interaction between the user and the program is robust and resistant to incorrect input. In the case of incorrect input the user is guided to give correct input and in the case of exceptions outside of the control of the user, the user is informed about the problem and given a detailed description of what went wrong.

MVC Architecture
The program is designed using a Model View Controller architecture model that allows for the interchangeability of Views, Models, Repositories and Exporters. This allows for any developers to easily add new features and segments to the application.

One More Thing (Notion Integration)
The application has a real time connection to Notion thanks to the Notion API. Any changes made by the user in jTasks are reflected in Notion and vice versa.  

