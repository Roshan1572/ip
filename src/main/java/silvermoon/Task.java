package silvermoon;

public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a new Task with the given description.
     *
     * @param description A non-null, non-blank description of the task.
     * @throws AssertionError if {@code description} is null or blank.
     */
    public Task(String description) {
        assert description != null && !description.isBlank() : "Task description must be non-empty";
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns a status icon indicating completion.
     *
     * @return "X" if the task is done, otherwise a space.
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns the description of this task.
     *
     * @return The task description.
     */
    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }
}
