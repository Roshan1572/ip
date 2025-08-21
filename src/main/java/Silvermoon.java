import java.util.Scanner;

public class Silvermoon {
    private static final String NAME = "Silvermoon";
    private static final String LINE = "____________________________________________________________";
    private static final int MAX_TASKS = 100;
    private static final String[] tasks = new String[MAX_TASKS];
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
            } else if (!input.isEmpty()) {
                addTask(input);
            }
            // ignore empty lines
        }
    }

    private static void addTask(String task) {
        if (size >= MAX_TASKS) {
            System.out.println(LINE);
            System.out.println(" Sorry, I can only remember up to " + MAX_TASKS + " tasks.");
            System.out.println(LINE);
            return;
        }
        tasks[size++] = task;
        System.out.println(LINE);
        System.out.println(" added: " + task);
        System.out.println(LINE);
    }

    private static void listTasks() {
        System.out.println(LINE);
        for (int i = 0; i < size; i++) {
            System.out.println(" " + (i + 1) + ". " + tasks[i]);
        }
        System.out.println(LINE);
    }

    private static void exit() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(LINE);
    }
}


