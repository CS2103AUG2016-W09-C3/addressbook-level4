package todoit.taskbook.logic.commands;

import todoit.taskbook.commons.core.EventsCenter;
import todoit.taskbook.commons.core.Messages;
import todoit.taskbook.commons.core.UnmodifiableObservableList;
import todoit.taskbook.commons.events.ui.JumpToListRequestEvent;
import todoit.taskbook.model.task.ReadOnlyTask;

/**
 * Selects a task identified using it's last displayed index from the task book.
 */
public class SelectCommand extends Command {

    public final int targetIndex;

    public static final String COMMAND_WORD = "select";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the task identified by the index number used in the last task listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_TASK_SUCCESS = "Selected Task: %1$s";

    public SelectCommand(int targetIndex) {
        this.targetIndex = targetIndex;
    }

    @Override
    public CommandResult execute() {

        UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();

        if (lastShownList.size() < targetIndex) {
            indicateAttemptToExecuteIncorrectCommand();
            return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
        }

        EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex - 1));
        return new CommandResult(String.format(MESSAGE_SELECT_TASK_SUCCESS, targetIndex));

    }
    

    @Override
    public boolean createsNewState() {
        return false;
    }

}
