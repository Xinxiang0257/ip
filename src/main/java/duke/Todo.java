package duke;

/**
 * Represents a todo task without any date/time attached to it.
 * Inherits from the duke.Task class.
 */
public class Todo extends Task {

    /**
     * Constructs a new duke.Todo task with the given description.
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

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (obj == null || getClass() != obj.getClass()) return false;
        return true; // Todos only need description comparison
    }
}