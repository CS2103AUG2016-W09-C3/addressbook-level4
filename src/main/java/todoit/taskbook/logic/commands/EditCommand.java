//@@author A0139046E
package todoit.taskbook.logic.commands;

import java.util.HashSet;
import java.util.Set;

import todoit.taskbook.commons.core.Messages;
import todoit.taskbook.commons.core.UnmodifiableObservableList;
import todoit.taskbook.commons.exceptions.IllegalValueException;
import todoit.taskbook.model.tag.Tag;
import todoit.taskbook.model.tag.UniqueTagList;
import todoit.taskbook.model.task.DateTime;
import todoit.taskbook.model.task.DatedTask;
import todoit.taskbook.model.task.DoneFlag;
import todoit.taskbook.model.task.Information;
import todoit.taskbook.model.task.Length;
import todoit.taskbook.model.task.Name;
import todoit.taskbook.model.task.Priority;
import todoit.taskbook.model.task.ReadOnlyDatedTask;
import todoit.taskbook.model.task.ReadOnlyTask;
import todoit.taskbook.model.task.Recurrence;
import todoit.taskbook.model.task.Task;
import todoit.taskbook.model.task.UniqueTaskList.DuplicateTaskException;
import todoit.taskbook.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Edit a tasks in the Task book.
 */

public class EditCommand extends Command {

	public static final String COMMAND_WORD = "edit";

	public static final String[] REQUIRED_PARAMS = {};

	public static final String MESSAGE_USAGE = COMMAND_WORD
			+ ": Edits the task identified by the index number used in the last task listing.\n"
			+ "Parameters: INDEX (must be a positive integer) [n/NAME] [d/DATE,TIME l/LENGTH] [r/RECUR] [p/PRIORITY] [i/INFORMATION] [t/TAG]...\n"
			+ "Example: " + COMMAND_WORD + " 1 d/next thurs 2pm";

	public static final String MESSAGE_EDIT_TASK_SUCCESS = "Edited Task: %1$s";
	public static final String MESSAGE_DUPLICATE_TASK = "This task already exists in the task book";
	public static final String MESSAGE_DATED_PARAMS = "A non-dated task cannot have length or recurring data.";

	public final int targetIndex;

	private Task toAdd;
	private ReadOnlyTask taskToEdit;
	private DateTime oldDatetime;
	private String name, datetime, length, recurring, priority, information, doneFlag;
	private Set<Tag> tagSet;
	private UniqueTagList tagList;

	public EditCommand(int targetIndex, String name, String datetime, String length, String recurring, String priority,
			String information, String doneFlag, Set<String> tags) throws IllegalValueException {
		this.targetIndex = targetIndex;
		final Set<Tag> tagSet = new HashSet<>();
		for (String tagName : tags) {
			tagSet.add(new Tag(tagName));
		}
		this.name = name;
		this.datetime = datetime;
		this.length = length;
		this.recurring = recurring;
		this.priority = priority;
		this.information = information;
		this.doneFlag = doneFlag;
		this.tagSet = tagSet;
	}

	@Override
	public CommandResult execute() {
		UnmodifiableObservableList<ReadOnlyTask> lastShownList = model.getFilteredTaskList();
		if (lastShownList.size() < targetIndex) {
			indicateAttemptToExecuteIncorrectCommand();
			return new CommandResult(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX);
		}
		taskToEdit = lastShownList.get(targetIndex - 1);
		setDoneFlag();
		boolean isDated = taskToEdit.isDated();
		if (isDated) {
			copyDatedTask(lastShownList);
		}
		copyTask();
		editTag();
		boolean typeChange = checkChangeTaskType();
		try {
			if (isDated) {
				this.toAdd = new DatedTask(new Name(name), new DateTime(datetime, oldDatetime), new Length(length),
						new Recurrence(recurring), new Priority(priority), new Information(information),
						new DoneFlag(doneFlag), new UniqueTagList(tagList));
			} else if (typeChange) {
				this.toAdd = new DatedTask(new Name(name), new DateTime(datetime), new Length(length),
						new Recurrence(recurring), new Priority(priority), new Information(information),
						new DoneFlag(doneFlag), new UniqueTagList(tagList));
			} else {
				if (checkHasDatedParams()) {
					throw new IllegalValueException(MESSAGE_DATED_PARAMS);
				}
				this.toAdd = new Task(new Name(name), new Priority(priority), new Information(information),
						new DoneFlag(doneFlag), new UniqueTagList(tagList));
			}
		} catch (IllegalValueException ive) {
			return new CommandResult(ive.getMessage());
		}

		try {
			model.deleteTask(taskToEdit);
			model.addTaskToIndex(toAdd, targetIndex - 1);
		} catch (TaskNotFoundException e) {
			assert false : "The target task cannot be missing";
		} catch (DuplicateTaskException e) {
			return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}

		return new CommandResult(String.format(MESSAGE_EDIT_TASK_SUCCESS, toAdd));
	}

	/**
	 * Check if task change from task to datedtask
	 */
	private boolean checkChangeTaskType() {
		if (!(this.datetime.equals("-1"))) {
			return true;
		}
		return false;
	}

	/**
	 * Check has dated params
	 */
	private boolean checkHasDatedParams() {
		if (!(this.length.equals(Length.NO_INTERVAL)) && !(this.recurring.equals(Recurrence.NO_INTERVAL))) {
			return true;
		}
		return false;
	}

	/**
	 * Copy dated task information of time, date, length, recurring if it is not
	 * edited.
	 */
	private void copyDatedTask(UnmodifiableObservableList<ReadOnlyTask> lastShownList) {
		ReadOnlyDatedTask datedTaskToEdit = (ReadOnlyDatedTask) lastShownList.get(targetIndex - 1);
		this.oldDatetime = datedTaskToEdit.getDateTime();
		if (this.datetime.equals("-1")) {
			this.datetime = datedTaskToEdit.getDateTime().toString();
		}
		if (this.length.equals(Length.NO_INTERVAL)) {
			this.length = datedTaskToEdit.getLength().toString();
		}
		if (this.recurring.equals(Recurrence.NO_INTERVAL)) {
			this.recurring = datedTaskToEdit.getRecurrence().toString();
		}
	}

	/**
	 * Copy task information of name, priority, information if it is not edited.
	 */
	private void copyTask() {
		if (this.name.equals(""))
			this.name = taskToEdit.getName().toString();
		if (this.priority.equals(""))
			this.priority = taskToEdit.getPriority().toString();
		if (this.information.equals(""))
			this.information = taskToEdit.getInformation().toString();
	}

	/**
	 * Edit the tag of task
	 */
	private void editTag() {
		if (this.tagSet.isEmpty()) {
			tagList = new UniqueTagList(tagSet);
			tagList.setTags(taskToEdit.getTags());
		} else {
			tagList = new UniqueTagList(tagSet);
		}
	}

	/**
	 * Set DoneFlag for done task
	 */
	private void setDoneFlag() {
		if (taskToEdit.getDoneFlag().isDone()) {
			this.doneFlag = DoneFlag.DONE;
		}
	}

	@Override
	public boolean createsNewState() {
		return true;
	}
}
// @@author