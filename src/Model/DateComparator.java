package Model;

import java.util.Comparator;

public class DateComparator implements Comparator<Task> {

    // Used for sorting by dates
    @Override
    public int compare(Task first, Task second) {
        return first.getDate().compareTo(second.getDate());
    }
    
}
