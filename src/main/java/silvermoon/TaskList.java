package silvermoon;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the list of tasks and provides operations to mutate/read it.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() { this.tasks = new ArrayList<>(); }

    /** Creates a task list initialised with {@code initial}. */
    public TaskList(List<Task> initial) { this.tasks = new ArrayList<>(initial); }

    /** Adds {@code t} to the end of the list. */
    public void add(Task t) { tasks.add(t); }

    /**
     * Removes and returns the task at the given zero-based {@code idx}.
     * @throws IndexOutOfBoundsException if {@code idx} is out of range
     */
    public Task removeAt(int idx) { return tasks.remove(idx); }

    /** Returns the task at zero-based {@code idx}. */
    public Task get(int idx) { return tasks.get(idx); }

    /** Returns the number of tasks in the list. */
    public int size() { return tasks.size(); }

    /** Returns the underlying list view (for storage/UI). */
    public List<Task> asList() { return tasks; }
}
