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
            } else if (input.startsWith("todo ")) {
                String desc = input.substring(5).trim();
                if (desc.isEmpty()) {
                    errorBox("Description for todo cannot be empty.");
                } else {
                    addAndAcknowledge(new ToDo(desc));
                }
            } else if (input.startsWith("deadline ")) {
                handleDeadline(input.substring(9).trim());
            } else if (input.startsWith("event ")) {
                handleEvent(input.substring(6).trim());
            } else if (!input.isEmpty()) {
                addAndAcknowledge(new ToDo(input));
            }
        }
    }

    private static void handleDeadline(String rest) {
        int p = rest.indexOf("/by");
        if (p < 0) {
            errorBox("Usage: deadline <description> /by <when>");
            return;
        }
        String desc = rest.substring(0, p).trim();
        String by = rest.substring(p + 3).trim(); // after "/by"
        if (desc.isEmpty() || by.isEmpty()) {
            errorBox("Usage: deadline <description> /by <when>");
            return;
        }
        addAndAcknowledge(new Deadline(desc, by));
    }

    private static void handleEvent(String rest) {
        int pf = rest.indexOf("/from");
        int pt = rest.indexOf("/to");
        if (pf < 0 || pt < 0 || pt < pf) {
            errorBox("Usage: event <description> /from <start> /to <end>");
            return;
        }
        String desc = rest.substring(0, pf).trim();
        String from = rest.substring(pf + 5, pt).trim(); // between "/from" and "/to"
        String to = rest.substring(pt + 3).trim();       // after "/to"
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            errorBox("Usage: event <description> /from <start> /to <end>");
            return;
        }
        addAndAcknowledge(new Event(desc, from, to));
    }

    private static void addAndAcknowledge(Task t) {
        if (size >= MAX_TASKS) {
            errorBox("Sorry, I can only remember up to " + MAX_TASKS + " tasks.");
            return;
        }
        tasks[size++] = t;
        System.out.println(LINE);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + t);
        System.out.println(" Now you have " + size + " task" + (size == 1 ? "" : "s") + " in the list.");
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


