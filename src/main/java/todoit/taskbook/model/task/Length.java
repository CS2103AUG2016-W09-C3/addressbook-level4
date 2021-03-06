// @@author A0140155U
package todoit.taskbook.model.task;

import todoit.taskbook.commons.exceptions.IllegalValueException;

/**
 * Represents a Task's length.
 * Functions as a wrapper for TimeInterval
 * 
 * A task can have no length, in which case hasInterval will be set to false,
 * and timeInterval will be given a dummy value.
 */
public class Length {

    private final TimeInterval timeInterval;
    public final static String NO_INTERVAL = "";
    public final static String DEFAULT_INTERVAL = "1h";
    public final static String PARAM_NOT_SPECIFIED = "-1";
    private boolean hasInterval = true;
    /**
     * Stores given interval. Validation of interval is done by TimeInterval class.
     *
     * @throws IllegalValueException if given information string is invalid.
     */
    public Length(String interval) throws IllegalValueException {
        assert interval != null;
        interval = interval.trim();
        
        if(interval.equals(NO_INTERVAL)){
            // Task has no length. Give it a dummy value
            hasInterval = false;
            interval = DEFAULT_INTERVAL;
        }
        this.timeInterval = new TimeInterval(interval);
    }

    public boolean isValid(){
        return hasInterval;
    }
    
    @Override
    public String toString() {
        if(!hasInterval){
            return NO_INTERVAL;
        }else{
            return this.timeInterval.toString();
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Length // instanceof handles nulls
                && this.timeInterval.equals(((Length) other).timeInterval)); // state check
    }

    @Override
    public int hashCode() {
        return this.timeInterval.hashCode();
    }
    
    public int getAsMinutes(){
        return timeInterval.getAsMinutes();
    }
}
