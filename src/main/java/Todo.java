/**
 * Represents a todo task without any date/time attached to it.
 * Inherits from the Task class.
 */
public class Todo extends Task {

    /**
     * Constructs a new Todo task with the given description.
     *
     * @param description The description of the todo task.
     */
    public Todo(String description) {
        super(description);
    }

    @Override
    public String getType() {
        return "T";
    }
}