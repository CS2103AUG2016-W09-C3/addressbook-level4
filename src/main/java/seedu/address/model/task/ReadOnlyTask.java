package seedu.address.model.task;

import seedu.address.model.tag.UniqueTagList;

/**
 * A read-only immutable interface for a Task in the addressbook(change to taskBook once refactor).
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyTask {

    Name getName();
    Recurrance getRecurrance();
    Priority getPriority();
    Information getInformation();

    /**
     * The returned TagList is a deep copy of the internal TagList,
     * changes on the returned list will not affect the task's internal tags.
     */
    UniqueTagList getTags();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyTask other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getName().equals(this.getName()) // state checks here onwards
                && other.getRecurrance().equals(this.getRecurrance())
                && other.getPriority().equals(this.getPriority())
                && other.getInformation().equals(this.getInformation()));
    }

    /**
     * Formats the task as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Recurrance: ")
                .append(getRecurrance())
                .append(" Priority: ")
                .append(getPriority())
                .append(" Information: ")
                .append(getInformation())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

    /**
     * Returns a string representation of this Task's tags
     */
    default String tagsString() {
        final StringBuffer buffer = new StringBuffer();
        final String separator = ", ";
        getTags().forEach(tag -> buffer.append(tag).append(separator));
        if (buffer.length() == 0) {
            return "";
        } else {
            return buffer.substring(0, buffer.length() - separator.length());
        }
    }

}
