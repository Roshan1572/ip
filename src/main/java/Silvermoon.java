import java.util.Scanner;

public class Silvermoon {
    private static final String NAME = "Silvermoon";
    private static final String LINE = "____________________________________________________________";

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
            String input = sc.nextLine();
            if (input.equals("bye")) {
                exit();
                break;
            }
            echo(input);
        }
    }

    private static void echo(String msg) {
        System.out.println(LINE);
        System.out.println(" " + msg);
        System.out.println(LINE);
    }

    private static void exit() {
        System.out.println(LINE);
        System.out.println(" Bye. Hope to see you again soon!");
    }
}

