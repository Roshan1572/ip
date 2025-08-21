import java.util.Scanner;

public class Silvermoon {
    private static final String NAME = "Silvermoon";
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final Task[] tasks = new Task[MAX_TASKS];
    private static int size = 0;

    public static void main(String[] args) {
        greet();
        run();
    }

    private static void greet() {
        System.out.println(LINE);
        System.out.println(" Hello! I'm " + NAME);
        System.out.println(" What can I do for you?");
        System.out.println(LINE);
    }

    private static void run() {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine().trim();
            if (input.equals("bye")) {
                exit();
                break;
            } else if (input.equals("list")) {
                listTasks();
            } else if (input.startsWith("mark ")) {
                handleMark(input);
            } else if (input.startsWith("unmark ")) {
                handleUnmark(input);
            } else if (!input.isEmpty()) {
                addTask(input);
            }
            // ignore empty lines
        }
    }

    private static void addTask(String description) {
        if (size >= MAX_TASKS) {
            System.out.println(LINE);
            System.out.println(" Sorry, I can only remember up to " + MAX_TASKS + " tasks.");
            System.out.println(LINE);
            return;
        }
        tasks[size++] = new Task(description);
        System.out.println(LINE);
        System.out.println(" added: " + description);
        System.out.println(LINE);
    }

    private static void listTasks() {
        System.out.println(LINE);
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < size; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void handleMark(String input) {
        Integer idx = parseIndex(input, "mark");
        if (idx == null) return;
        Task t = tasks[idx];
        t.markAsDone();
        System.out.println(LINE);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    private static void handleUnmark(String input) {
        Integer idx = parseIndex(input, "unmark");
        if (idx == null) return;
        Task t = tasks[idx];
        t.markAsUndone();
        System.out.println(LINE);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + t);
        System.out.println(LINE);
    }

    private static Integer parseIndex(String input, String cmd) {
        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            errorBox("Usage: " + cmd + " <taskNumber>");
            return null;
        }
        try {
            int oneBased = Integer.parseInt(parts[1]);
            int idx = oneBased - 1;
            if (idx < 0 || idx >= size) {
                errorBox("Task number must be between 1 and " + size + ".");
                return null;
            }
            return idx;
        } catch (NumberFormatException e) {
            errorBox("Task number must be an integer.");
            return null;
        }
    }

    private static void errorBox(String msg) {
        System.out.println(LINE);
        System.out.println(" " + msg);
        System.out.println(LINE);
    }

    private static void exit() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}



