package duke;

import java.time.LocalDateTime;

/**
 * Represents a deadline task that needs to be done before a specific date/time.
 * Inherits from the duke.Task class.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    /**
     * Constructs a new duke.Deadline task with the given description and deadline.
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

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (obj == null || getClass() != obj.getClass()) return false;
        Deadline other = (Deadline) obj;
        return this.by.equals(other.by); // Also compare due dates
    }
}

