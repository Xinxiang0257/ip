package duke;

/**
 * Represents a task with a description and completion status.
 * Provides methods to mark tasks as done or not done, and to display
 * the task in a formatted string.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Constructs a new duke.Task with the given description.
     * The task is initially marked as not done.
     *
     * @param description The description of the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Returns the status icon representing the completion state of the task.
     * "X" indicates completed, space indicates not completed.
     *
     * @return The status icon as a string.
     */

    public boolean isDone() {
        return this.isDone;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the task type icon.
     *
     * @return The task type icon as a string.
     */
    public String getType() {
        return " "; // Default for base duke.Task class
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        this.isDone = false;
    }

    /**
     * Returns additional information about the task.
     *
     * @return Additional task information as a string.
     */
    public String getAdditionalInfo() {
        return "";
    }

    @Override
    public String toString() {
        String additionalInfo = getAdditionalInfo();
        if (!additionalInfo.isEmpty()) {
            return "[" + getType() + "][" + getStatusIcon() + "] " + description + " " + additionalInfo;
        }
        return "[" + getType() + "][" + getStatusIcon() + "] " + description;
    }

//    public String toString() {
//        return " " + description;
//    }
}