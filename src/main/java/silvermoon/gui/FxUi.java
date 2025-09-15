package silvermoon.gui;

import silvermoon.Task;
import silvermoon.Ui;

import java.util.List;
import java.util.function.Consumer;

/** GUI Ui: writes messages into the transcript via Consumer. */
public class FxUi extends Ui {
    private final Consumer<String> out;

    public FxUi(Consumer<String> out) {
        this.out = out;
    }

    @Override
    public void showGreeting(String name) {
        out.accept("Anar'alah belore! I'm " + name + "\nHow may I assist you?");
    }

    @Override
    public void showExit() {
        out.accept("Band'or shorel'aran, farewell champion.");
    }

    @Override
    public void showMessage(String msg) {
        out.accept(msg);
    }

    @Override
    public void showError(String msg) {
        out.accept("⚠ " + msg);
    }

    @Override
    public void showTaskAdded(Task t, int count) {
        out.accept("I've added this quest, adventurer:\n  " + t
                + "\nNow you have " + count + " quest" + (count == 1 ? "" : "s") + ".");
    }

    @Override
    public void showTaskRemoved(Task t, int count) {
        out.accept("Noted. I've removed this quest, adventurer:\n  " + t
                + "\nNow you have " + count + " quest" + (count == 1 ? "" : "s") + ".");
    }

    @Override
    public void showTaskMarked(Task t, boolean done) {
        out.accept((done ? "Anar'alah! Marked as done:\n  " : "OK, marked as not done:\n  ") + t);
    }

    @Override
    public void showTaskList(List<Task> tasks) {
        StringBuilder sb = new StringBuilder("Here are the quests in your log:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        out.accept(sb.toString().trim());
    }
    @Override
    public void showMatchingTasks(java.util.List<Task> tasks) {
        StringBuilder sb = new StringBuilder("Here are the matching quests in your log:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(" ").append(i + 1).append(". ").append(tasks.get(i)).append("\n");
        }
        out.accept(sb.toString().trim());
    }
}

