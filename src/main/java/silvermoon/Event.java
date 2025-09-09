package silvermoon;

/**
 * Creates event with specified timing.
 */
public class Event extends Task {
    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        assert from != null && !from.isBlank() : "Event 'from' must be non-empty";
        assert to != null && !to.isBlank()     : "Event 'to' must be non-empty";
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }
}
