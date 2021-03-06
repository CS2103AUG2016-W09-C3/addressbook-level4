package todoit.taskbook.model.task;

import java.util.Objects;

import todoit.taskbook.commons.util.CollectionUtil;
import todoit.taskbook.model.tag.UniqueTagList;

/**
 * Represents a Task with date, time and length in the task book.
 * Guarantees: details are present and not null, field values are validated.
 */

public class DatedTask extends Task implements ReadOnlyDatedTask {
    
    private DateTime dateTime;
    private Length length;
    private Recurrence recurrence;
    
    private UniqueTagList tags;
    
    /**
     * Dafault constructor, should not be used.
     */
    protected DatedTask(){
        ;
    }
    
    /**
     * Every field must be present and not null.
     */
    public DatedTask(Name name, DateTime time, Length length, Recurrence recurring, Priority priority,
            Information information, DoneFlag doneFlag, UniqueTagList uniqueTagList) {
        super(name, priority, information, doneFlag, uniqueTagList);
        assert !CollectionUtil.isAnyNull(recurring, time, length);
        this.dateTime = time;
        this.length = length;
        this.recurrence = recurring;
    }
    
    /**
     * Copy constructor.
     */
    public DatedTask(ReadOnlyDatedTask source) {
        this(source.getName(), source.getDateTime(), source.getLength(), 
             source.getRecurrence(), source.getPriority(), source.getInformation(), source.getDoneFlag(), source.getTags());
    }
    
    public DateTime getDateTime(){
        return this.dateTime;
    }

    @Override
    public boolean hasValidLength(){
        return this.length.isValid();
    }
    
    @Override
    public DateTime getDateTimeEnd() {
        if(!hasValidLength()){
            return dateTime;
        }
        return new DateTime(dateTime.getDateTime().plusMinutes(length.getAsMinutes()));
    }
    
    public Length getLength(){
        return this.length;
    }

    public Recurrence getRecurrence() {
        return this.recurrence;
    }

    @Override
    public String toString() {
        return getAsText();
    }
    

    
    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.name, this.dateTime, this.length, 
                            this.recurrence, this.priority, this.information, tags);
    }

}
