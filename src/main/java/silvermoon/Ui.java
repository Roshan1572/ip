package silvermoon;

import java.util.List;

/** Deals with user-facing output. */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    public void showGreeting(String name) {
        System.out.println(LINE);
        System.out.println(" Hello! I'm " + name);
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    public void showExit() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }

    public void showLine() {
        System.out.println(LINE);
    }

    public void showMessage(String msg) {
        System.out.println(" " + msg);
    }

    public void showError(String msg) {
        System.out.println(LINE);
        System.out.println(" " + msg);
        System.out.println(LINE);
    }

    public void showTaskAdded(Task t, int count) {
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " task" + (count == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    public void showTaskRemoved(Task t, int count) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " task" + (count == 1 ? "" : "s") + " in the list.");
        System.out.println(LINE);
    }

    public void showTaskMarked(Task t, boolean done) {
        System.out.println(LINE);
        System.out.println(done
                ? " Nice! I've marked this task as done:"
                : " OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    public void showMatchingTasks(java.util.List<Task> tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the matching tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }
}
