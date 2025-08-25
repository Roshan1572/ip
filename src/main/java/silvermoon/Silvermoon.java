package silvermoon;

import java.io.IOException;
import java.util.Scanner;

public class Silvermoon {
    private static final String NAME = "Silvermoon";

    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public Silvermoon(String storageFileName) {
        this.ui = new Ui();
        this.storage = new Storage(storageFileName);
        try {
            this.tasks = new TaskList(storage.load());
        } catch (IOException e) {
            ui.showError("Couldn't load saved tasks. I'll start fresh.");
            this.tasks = new TaskList();
        }
    }

    public void run() {
        ui.showGreeting(NAME);
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            try {
                boolean shouldExit = Parser.parseAndExecute(input, tasks, ui, storage);
                if (shouldExit) break;
            } catch (SilvermoonException e) {
                ui.showError(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        new Silvermoon("silvermoon.txt").run(); // stored under ./data/silvermoon.txt by Storage
    }
}

