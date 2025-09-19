/**
 * Represents an event task that starts and ends at specific date/times.
 * Inherits from the Task class.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    /**
     * Constructs a new Event task with the given description, start and end times.
     *
     * @param description The description of the event task.
     * @param from The start date/time of the event as a string.
     * @param to The end date/time of the event as a string.
     */
    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
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