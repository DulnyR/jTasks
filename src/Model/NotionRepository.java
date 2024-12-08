package Model;

import notion.api.v1.NotionClient;
import notion.api.v1.http.OkHttp5Client;
import notion.api.v1.logging.Slf4jLogger;
import notion.api.v1.model.databases.QueryResults;
import notion.api.v1.model.pages.Page;
import notion.api.v1.model.pages.PageParent;
import notion.api.v1.model.pages.PageProperty;
import notion.api.v1.model.pages.PageProperty.RichText;
import notion.api.v1.model.pages.PageProperty.RichText.Text;
import notion.api.v1.request.databases.QueryDatabaseRequest;
import notion.api.v1.request.pages.CreatePageRequest;
import notion.api.v1.request.pages.UpdatePageRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NotionRepository implements IRepository {

    private final NotionClient client;
    private final String databaseId;
    private final String titleColumnName = "Identifier";
   
    public NotionRepository(String apiKey, String databaseId) throws RepositoryException{
        // Create Notion Client
		this.client = new NotionClient(apiKey);

        // Configure HTTP Client
        client.setHttpClient(new OkHttp5Client(60000,60000,60000));

        // Configure Logger
        client.setLogger(new Slf4jLogger());

        // Activate Notion API Logger
        System.setProperty("notion.api.v1.logging.StdoutLogger", "debug");

        this.databaseId = databaseId;

        try {
            getTaskHashMap();
        } catch (RepositoryException e) {
            throw e;
        }
	}

	public Task addTask(Task task) {
        // Create page properties
        Map<String, PageProperty> properties = getProperties(task);

        // Configure parent page of database
        PageParent parent = PageParent.database(databaseId);

        // Create Notion API request
        CreatePageRequest request = new CreatePageRequest(parent, properties);

        client.createPage(request);

        return task;
    }

    public Task getTask(String identifier) throws RepositoryException {
        Map<String, Task> tasks = getTaskHashMap();
        return tasks.get(identifier);
    }

    public void removeTask(String identifier) throws RepositoryException {
        try {
            String pageId = findPageIdByIdentifier(identifier, titleColumnName);
            if (pageId == null) {
                throw new RepositoryException("Could not find page ID with identifier.");
            }

            // Archive task
            UpdatePageRequest updatePageRequest = new UpdatePageRequest(pageId, Collections.emptyMap(), true);
            client.updatePage(updatePageRequest);
        } catch(Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    public boolean taskExists(String identifier) throws RepositoryException {
        Map<String, Task> tasks = getTaskHashMap();
        return tasks.containsKey(identifier);
    }

    // Get all registered tasks
    public ArrayList<Task> getAllTasks() throws RepositoryException {
        return new ArrayList<>(getTaskHashMap().values());
    }

    // Allows for faster look up times in other functions
    private Map<String, Task> getTaskHashMap() throws RepositoryException {
        Map<String, Task> tasks = new HashMap<>();
        try {
            // Create database request
            QueryDatabaseRequest queryDatabaseRequest = new QueryDatabaseRequest(databaseId);

            // Execute the request
            QueryResults queryResults = client.queryDatabase(queryDatabaseRequest);

            // Process results
            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                Task task = mapPageToTask(page.getId(), properties);
                if (task != null) {
                    tasks.put(task.getIdentifier(), task);
                }
            }
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
        return tasks;
    }

    public void modifyTask(Task task) throws RepositoryException {
        try {
            String pageId = findPageIdByIdentifier(task.getIdentifier(), titleColumnName);
            if (pageId == null) {
                throw new RepositoryException("Could not find page ID with identifier.");
            }

            // Create current properties
            Map<String, PageProperty> updatedProperties = getProperties(task);

            // Create update request
            UpdatePageRequest updatePageRequest = new UpdatePageRequest(pageId, updatedProperties);
            client.updatePage(updatePageRequest);
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }

    private Map<String, PageProperty> getProperties(Task task) {
        return Map.of(
            "Identifier", createTitleProperty(task.getIdentifier()),
            "Title", createRichTextProperty(task.getTitle()),
            "Date", createDateProperty(task.getDate()),
            "Content", createRichTextProperty(task.getContent()),
            "Priority", createNumberProperty(task.getPriority()),
            "Estimated Duration", createNumberProperty(task.getEstimatedDuration()),
            "Completed", createCheckboxProperty(task.isCompleted())
        );
    }

    private String findPageIdByIdentifier(String identifier, String titleColumnName) throws RepositoryException {
        try {
            QueryDatabaseRequest queryDatabaseRequest = new QueryDatabaseRequest(databaseId);
            QueryResults queryResults = client.queryDatabase(queryDatabaseRequest);

            for (Page page : queryResults.getResults()) {
                Map<String, PageProperty> properties = page.getProperties();
                if (properties.containsKey(titleColumnName) && 
                    properties.get(titleColumnName).getTitle().get(0).getText().getContent().equals(identifier)) {
                    return page.getId();        	
                }     
            }
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
        return null;
    }

    private PageProperty createTitleProperty(String title) {
        RichText idText = new RichText();
        idText.setText(new Text(title));
        PageProperty idProperty = new PageProperty();
        idProperty.setTitle(Collections.singletonList(idText));
        return idProperty;
    }
    
    private PageProperty createRichTextProperty(String text) {
        RichText richText = new RichText();
        richText.setText(new Text(text));
        PageProperty property = new PageProperty();
        property.setRichText(Collections.singletonList(richText));
        return property;
    }

    private PageProperty createNumberProperty(Integer number) {
        PageProperty property = new PageProperty();
        property.setNumber(number);
        return property;
    }

    // Ensure same date format throughout program
    private PageProperty createDateProperty(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = dateFormat.format(date);
        PageProperty property = new PageProperty();
        PageProperty.Date dateProperty = new PageProperty.Date();
        dateProperty.setStart(dateString);
        property.setDate(dateProperty);
        return property;
    }

    private PageProperty createCheckboxProperty(boolean checked) {
        PageProperty property = new PageProperty();
        property.setCheckbox(checked);
        return property;
    }

    private Task mapPageToTask(String pageId, Map<String, PageProperty> properties) throws RepositoryException {
        try {
            String identifier = (properties.get("Identifier").getTitle().get(0).getText().getContent());
            String title = (properties.get("Title").getRichText().get(0).getText().getContent());

            // Convert String of date into Date
            String dateString = properties.get("Date").getDate().getStart();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = dateFormat.parse(dateString);

            String content = (properties.get("Content").getRichText().get(0).getText().getContent());
            int priority = (properties.get("Priority").getNumber().intValue());
            int estimatedDuration = (properties.get("Priority").getNumber().intValue());
            boolean completed = (properties.get("Completed").getCheckbox());
            return new Task(identifier, title, date, content, priority, estimatedDuration, completed);
        } catch (Exception e) {
            throw new RepositoryException(e.getMessage());
        }
    }
}
