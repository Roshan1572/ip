package silvermoon;

import java.util.List;

/** Deals with user-facing output. */
public class Ui {
    private static final String LINE = "____________________________________________________________";

    public void showGreeting(String name) {
        System.out.println(LINE);
        System.out.println("Anar'alah belore! I'm " + name);
        System.out.println("How may I assist you?");
        System.out.println(LINE);
    }

    public void showExit() {
        System.out.println(LINE);
        System.out.println("Band'or shorel'aran, farewell champion.");
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
        System.out.println("I've added this quest, adventurer:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " quest" + (count == 1 ? "" : "s") + " in the log.");
        System.out.println(LINE);
    }

    public void showTaskRemoved(Task t, int count) {
        System.out.println(LINE);
        System.out.println(" Noted. I've removed this quest, adventurer:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + count + " quest" + (count == 1 ? "" : "s") + " in the log.");
        System.out.println(LINE);
    }

    public void showTaskMarked(Task t, boolean done) {
        System.out.println(LINE);
        System.out.println(done
                ? " Anar'alah! Marked as done:"
                : " OK, I've marked this quest as not done yet:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    public void showTaskList(List<Task> tasks) {
        System.out.println(LINE);
        System.out.println(" Here are the quests in your log:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println(LINE);
    }

    public void showMatchingTasks(java.util.List<Task> tasks) {
        System.out.println("____________________________________________________________");
        System.out.println(" Here are the matching quests in your log:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + ". " + tasks.get(i));
        }
        System.out.println("____________________________________________________________");
    }
}
