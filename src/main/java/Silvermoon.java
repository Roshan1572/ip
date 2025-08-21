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
            }
            if (input.isEmpty()) {
                continue;
            }
            try {
                process(input);
            } catch (SilvermoonException e) {
                errorBox(e.getMessage());
            }
        }
    }

    private static void process(String input) throws SilvermoonException {
        if (input.equals("list")) {
            listTasks();
            return;
        }
        if (input.startsWith("mark ")) {
            handleMark(input);
            return;
        }
        if (input.startsWith("unmark ")) {
            handleUnmark(input);
            return;
        }
        if (input.startsWith("todo")) {
            String rest = input.length() >= 4 ? input.substring(4).trim() : "";
            if (rest.isEmpty()) {
                throw new SilvermoonException("Oops! The description of a todo cannot be empty.");
            }
            addAndAcknowledge(new ToDo(rest));
            return;
        }
        if (input.startsWith("deadline")) {
            handleDeadline(input.substring(8).trim());
            return;
        }
        if (input.startsWith("event")) {
            handleEvent(input.substring(5).trim());
            return;
        }
        // Unknown command
        throw new SilvermoonException("Sorry, I don't recognize that command.");
    }

    private static void handleDeadline(String rest) throws SilvermoonException {
        int p = rest.indexOf("/by");
        if (p < 0) {
            throw new SilvermoonException("Usage: deadline <description> /by <when>");
        }
        String desc = rest.substring(0, p).trim();
        String by = rest.substring(p + 3).trim();
        if (desc.isEmpty() || by.isEmpty()) {
            throw new SilvermoonException("Usage: deadline <description> /by <when>");
        }
        addAndAcknowledge(new Deadline(desc, by));
    }

    private static void handleEvent(String rest) throws SilvermoonException {
        int pf = rest.indexOf("/from");
        int pt = rest.indexOf("/to");
        if (pf < 0 || pt < 0 || pt < pf) {
            throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
        }
        String desc = rest.substring(0, pf).trim();
        String from = rest.substring(pf + 5, pt).trim();
        String to = rest.substring(pt + 3).trim();
        if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
        }
        addAndAcknowledge(new Event(desc, from, to));
    }

    private static void handleMark(String input) throws SilvermoonException {
        int idx = parseIndex(input, "mark");
        tasks[idx].markAsDone();
        System.out.println(LINE);
        System.out.println(" Nice! I've marked this task as done:");
        System.out.println("   " + tasks[idx]);
        System.out.println(LINE);
    }

    private static void handleUnmark(String input) throws SilvermoonException {
        int idx = parseIndex(input, "unmark");
        tasks[idx].markAsUndone();
        System.out.println(LINE);
        System.out.println(" OK, I've marked this task as not done yet:");
        System.out.println("   " + tasks[idx]);
        System.out.println(LINE);
    }

    private static int parseIndex(String input, String cmd) throws SilvermoonException {
        String[] parts = input.split("\\s+");
        if (parts.length != 2) {
            throw new SilvermoonException("Usage: " + cmd + " <taskNumber>");
        }
        int oneBased;
        try {
            oneBased = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new SilvermoonException("Task number must be an integer.");
        }
        int idx = oneBased - 1;
        if (idx < 0 || idx >= size) {
            throw new SilvermoonException("Task number must be between 1 and " + size + ".");
        }
        return idx;
    }

    private static void addAndAcknowledge(Task t) throws SilvermoonException {
        if (size >= MAX_TASKS) {
            throw new SilvermoonException("Sorry, I can only remember up to " + MAX_TASKS + " tasks.");
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


