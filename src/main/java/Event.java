import java.time.LocalDateTime;

/**
 * Represents an event task that starts and ends at specific date/times.
 * Inherits from the Task class.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Constructs a new Event task with the given description, start and end times.
     *
     * @param description The description of the event task.
     * @param from        The start date/time of the event as a string.
     * @param to          The end date/time of the event as a string.
     */
    public Event(String description, String from, String to) throws CheesefoodException {
        super(description);
        this.from = DateTimeParser.parse(from);
        this.to = DateTimeParser.parse(to);
    }

    public String getFrom() {
        return DateTimeParser.formatForDisplay(from);
    }

    public String getTo() {
        return DateTimeParser.formatForDisplay(to);
    }

    @Override
    public String getType() {
        return "E";
    }

    @Override
    public String getAdditionalInfo() {
        return "(from: " + from + " to: " + to + ")";
    }

}
