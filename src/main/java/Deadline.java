/**
 * Represents a deadline task that needs to be done before a specific date/time.
 * Inherits from the Task class.
 */
public class Deadline extends Task {
    protected String by;

    /**
     * Constructs a new Deadline task with the given description and deadline.
     *
     * @param description The description of the deadline task.
     * @param by The deadline date/time as a string.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String getType() {
        return "D";
    }

    @Override
    public String getAdditionalInfo() {
        return "(by: " + by + ")";
    }
}