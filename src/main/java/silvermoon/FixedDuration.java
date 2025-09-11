package silvermoon;

import java.util.Objects;

/** A task that takes a fixed amount of time but has no fixed start/end. */
public class FixedDuration extends Task {
    private final String duration; // e.g., "2h", "90m"

    /**
     * @param description short description of the task
     * @param duration human-friendly duration like "2h", "90m"
     */
    public FixedDuration(String description, String duration) {
        super(description);
        assert duration != null && !duration.isBlank() : "duration must be non-empty";
        this.duration = duration;
    }

    public String getDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return "[F]" + super.toString() + " (takes: " + duration + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FixedDuration)) return false;
        FixedDuration that = (FixedDuration) o;
        return Objects.equals(description, that.description)
                && Objects.equals(isDone, that.isDone)
                && Objects.equals(duration, that.duration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, isDone, duration);
    }
}
