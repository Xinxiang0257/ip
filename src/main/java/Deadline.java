import java.time.LocalDateTime;

/**
 * Represents a deadline task that needs to be done before a specific date/time.
 * Inherits from the Task class.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a new Deadline task with the given description and deadline.
     *
     * @param description The description of the deadline task.
     * @param by          The deadline date/time as a string.
     */
    public Deadline(String description, String by) throws CheesefoodException {
        super(description);
        this.by = DateTimeParser.parse(by);
    }

    public String getBy() {
        return DateTimeParser.formatForDisplay(this.by);
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

