package guitests;

import org.junit.Test;
import seedu.address.commons.core.Messages;
import seedu.address.testutil.TestTask;

import static org.junit.Assert.assertTrue;

public class FindCommandTest extends TaskBookGuiTest {
    //@@author A0139121R
    @Test
    public void find_nonEmptyList() {
        assertFindResult("find Mark"); //no results
        assertFindResult("find Meier", td.breadShopping, td.danielLunch); //multiple results

        //find after deleting one result
        commandBox.runCommand("delete 1");
        assertFindResult("find Meier",td.danielLunch);
    }

    @Test
    public void find_emptyList(){
        commandBox.runCommand("clear");
        assertFindResult("find Jean"); //no results
    }

    @Test
    public void find_byName(){
        assertFindResult("find Meier s/name", td.danielLunch);
    }
    
    @Test
    public void find_byDate(){
        commandBox.runCommand(td.dinnerDate.getAddCommand());
        assertFindResult("find 17-11-2016 s/date", td.dinnerDate);
    }
    
    @Test
    public void find_byInformation(){
        assertFindResult("find loan s/information", td.lorryMaintainance);
    }
    //@@author
    @Test
    public void find_invalidCommand_fail() {
        commandBox.runCommand("findgeorge");
        assertResultMessage(Messages.MESSAGE_UNKNOWN_COMMAND);
    }
    

    private void assertFindResult(String command, TestTask... expectedHits ) {
        commandBox.runCommand(command);
        assertListSize(expectedHits.length);
        assertResultMessage(expectedHits.length + " tasks listed!");
        assertTrue(taskListPanel.isListMatching(expectedHits));
    }
}
