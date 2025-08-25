import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;

    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.tasks = new ArrayList<>(initial);
    }

    public void add(Task t) { tasks.add(t); }

    public Task removeAt(int idx) { return tasks.remove(idx); }

    public Task get(int idx) { return tasks.get(idx); }

    public int size() { return tasks.size(); }

    public List<Task> asList() { return tasks; }
}
