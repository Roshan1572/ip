package silvermoon;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds the list of tasks and provides operations to mutate/read it.
 */
public class TaskList {
    private final List<Task> tasks;

    /** Creates an empty task list. */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list initialised with the given tasks.
     *
     * @param initial initial tasks to populate the list with
     */
    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    /**
     * Adds a task to the end of the list.
     *
     * @param task the task to add
     */
    public void add(Task task) {
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the given zero-based index.
     *
     * @param idx zero-based index of the task to remove
     * @return the removed task
     * @throws IndexOutOfBoundsException if {@code idx} is out of range
     */
    public Task removeAt(int idx) {
        return tasks.remove(idx);
    }

    /**
     * Returns the task at the given zero-based index.
     *
     * @param idx zero-based index of the task to fetch
     * @return the task at {@code idx}
     * @throws IndexOutOfBoundsException if {@code idx} is out of range
     */
    public Task get(int idx) {
        return tasks.get(idx);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return task count
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns a live view of the underlying task list.
     * Changes to the returned list affect this {@code TaskList}.
     *
     * @return the underlying list view
     */
    public List<Task> asList() {
        return tasks;
    }
}
