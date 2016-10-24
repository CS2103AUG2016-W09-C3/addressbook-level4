package seedu.address.model.task;

import static org.junit.Assert.*;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;

public class TimeIntervalTest {

    @Test
    public void checkValidIntervals() {
        try {
            TimeInterval t;
            t = new TimeInterval("10m");
            assertEquals(t.getAsMinutes(), 10);
            t = new TimeInterval("4h");
            assertEquals(t.getAsMinutes(), 4 * 60);
            t = new TimeInterval("40h");
            assertEquals(t.getAsMinutes(), 40 * 60);
            t = new TimeInterval("2d");
            assertEquals(t.getAsMinutes(), 2 * 24 * 60);
            t = new TimeInterval("3w");
            assertEquals(t.getAsMinutes(), 3 * 7 * 24 * 60);
            
            // Check if TimeInterval is able to get different aliases
            t = new TimeInterval("6min");
            assertEquals(t.getAsMinutes(), 6);
            t = new TimeInterval("9mins");
            assertEquals(t.getAsMinutes(), 9);
            t = new TimeInterval("18hr");
            assertEquals(t.getAsMinutes(), 18 * 60);
            t = new TimeInterval("7hrs");
            assertEquals(t.getAsMinutes(), 7 * 60);
            t = new TimeInterval("55day");
            assertEquals(t.getAsMinutes(), 55 * 24 * 60);
            t = new TimeInterval("100days");
            assertEquals(t.getAsMinutes(), 100 * 24 * 60);
            t = new TimeInterval("2week");
            assertEquals(t.getAsMinutes(), 2 * 7 * 24 * 60);
            t = new TimeInterval("9weeks");
            assertEquals(t.getAsMinutes(), 9 * 7 * 24 * 60);
        } catch (IllegalValueException e) {
            fail("Could not parse time interval");
        }
    }
    
    @Test
    public void checkInvalidIntervals() {
        checkInvalidInterval("1");
        checkInvalidInterval("1 h");
        checkInvalidInterval("0.5h");
    }
    
    private void checkInvalidInterval(String invalidInterval){
        try {
            TimeInterval t;
            t = new TimeInterval(invalidInterval);
            fail("Successfully parsed wrong interval");
        } catch (IllegalValueException e) {
        }
    }
    
}
