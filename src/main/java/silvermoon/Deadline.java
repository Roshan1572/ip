package silvermoon;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * A task that must be completed by a specific date.
 */
public class Deadline extends Task {
    protected LocalDate by;

    public Deadline(String description, LocalDate by) {
        super(description);
        assert by != null : "Deadline date must be non-null!";
        this.by = by;
    }

    /** Returns the deadline date. */
    public LocalDate getBy() {
        return by;
    }

    /** Returns a user-friendly string with the formatted date. */
    @Override
    public String toString() {
        String nice = by.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        return "[D]" + super.toString() + " (by: " + nice + ")";
    }
}
