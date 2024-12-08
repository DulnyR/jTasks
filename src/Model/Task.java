package Model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Task implements Comparable<Task>, Serializable {
    private static final long serialVersionUID = 1L;
    private String identifier;
    private String title;
    private Date date;
    private String content;
    private int priority;
    private int estimatedDuration;
    private boolean completed;

    // Used for creation by user (ID Assigned)
    public Task(String title, String content, int priority, int estimatedDuration) {
        this(UUID.randomUUID().toString(), title, new Date(), content, priority, estimatedDuration, false);
    }

    // Used for reading in from files
    public Task(String identifier, String title, Date date, String content, int priority, int estimatedDuration, boolean completed) {
        this.identifier = identifier;
        this.title = title;
        this.date = date;
        this.content = content;
        this.priority = priority;
        this.estimatedDuration = estimatedDuration;
        this.completed = completed;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDate() {
        return date;
    }

    public String getDateString() {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getEstimatedDuration() {
        return estimatedDuration;
    }

    public void setEstimatedDuration(int estimatedDuration) {
        this.estimatedDuration = estimatedDuration;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public int compareDate(Task other) {
        return this.date.compareTo(other.date);
    }

    public String getTaskAsDelimitedString(String d) {
        return this.identifier + d + this.title + d + getDateString() + d + this.content +
                d + this.priority + d + this.estimatedDuration + d + this.completed;
    }

    // Standard factory builder
    public static Task getTaskFromDelimitedString(String string, String delimiter) {
        String[] chunks = string.split(delimiter);

        if(chunks.length != 7) {
            return null;
        }

        try {
            String identifier = chunks[0];
            String title = chunks[1];
            String[] dateChunks = chunks[2].split("-");
            @SuppressWarnings("deprecation")
            Date date = new Date(Integer.parseInt(dateChunks[0]), Integer.parseInt(dateChunks[1]) - 1, Integer.parseInt(dateChunks[2]));
            String content = chunks[3];
            int priority = Integer.parseInt(chunks[4]);
            int estimatedDuration = Integer.parseInt(chunks[5]);
            boolean completed = chunks[6].equals("true") ? true : false;
            return new Task(identifier, title, date, content, priority, estimatedDuration, completed);
        } catch (Exception e) {
            return null;
        }
    }

    // Used for sorting by priority
    @Override
    public int compareTo(Task other) {
        return Integer.compare(other.priority, this.priority);
    }
}
