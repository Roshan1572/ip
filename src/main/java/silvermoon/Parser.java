package silvermoon;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user input strings and executes the corresponding actions.
 */
public final class Parser {

    /** Prevents instantiation of utility class. */
    private Parser() {}

    /**
     * Parses and executes a single user input.
     *
     * @param input    raw line from the user
     * @param taskList list to operate on
     * @param ui       UI for user-visible output
     * @param storage  persistence for changes
     * @return {@code true} if the caller should exit; {@code false} otherwise
     * @throws SilvermoonException if the command is recognised but malformed
     */
    public static boolean parseAndExecute(String input, TaskList taskList, Ui ui, Storage storage)
            throws SilvermoonException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) {
            return false;
        }

        if (trimmed.equals("bye")) {
            ui.showExit();
            return true;
        }
        if (trimmed.equals("list")) {
            ui.showTaskList(taskList.asList());
            return false;
        }
        if (trimmed.startsWith("mark ")) {
            int idx = parseIndex(trimmed, "mark", taskList.size());
            taskList.get(idx).markAsDone();
            storageSafeSave(storage, taskList, ui);
            ui.showTaskMarked(taskList.get(idx), true);
            return false;
        }
        if (trimmed.startsWith("unmark ")) {
            int idx = parseIndex(trimmed, "unmark", taskList.size());
            taskList.get(idx).markAsUndone();
            storageSafeSave(storage, taskList, ui);
            ui.showTaskMarked(taskList.get(idx), false);
            return false;
        }
        if (trimmed.startsWith("delete ")) {
            int idx = parseIndex(trimmed, "delete", taskList.size());
            Task removed = taskList.removeAt(idx);
            storageSafeSave(storage, taskList, ui);
            ui.showTaskRemoved(removed, taskList.size());
            return false;
        }
        if (trimmed.startsWith("todo")) {
            String rest = trimmed.length() >= 4 ? trimmed.substring(4).trim() : "";
            if (rest.isEmpty()) {
                throw new SilvermoonException("The description of a todo cannot be empty.");
            }
            Task t = new ToDo(rest); // <- fixed name
            taskList.add(t);
            storageSafeSave(storage, taskList, ui);
            ui.showTaskAdded(t, taskList.size());
            return false;
        }
        if (trimmed.startsWith("deadline")) {
            String rest = trimmed.substring(8).trim();
            int byPos = rest.indexOf("/by");
            if (byPos < 0) {
                throw new SilvermoonException("Usage: deadline <description> /by <yyyy-MM-dd>");
            }
            String desc = rest.substring(0, byPos).trim();
            String byRaw = rest.substring(byPos + 3).trim();
            if (desc.isEmpty() || byRaw.isEmpty()) {
                throw new SilvermoonException("Usage: deadline <description> /by <yyyy-MM-dd>");
            }
            try {
                LocalDate byDate = LocalDate.parse(byRaw); // yyyy-MM-dd
                Task t = new Deadline(desc, byDate);
                taskList.add(t);
                storageSafeSave(storage, taskList, ui);
                ui.showTaskAdded(t, taskList.size());
            } catch (DateTimeParseException e) {
                throw new SilvermoonException("Please use date format yyyy-MM-dd (e.g., 2019-10-15).");
            }
            return false;
        }
        if (trimmed.startsWith("event")) {
            String rest = trimmed.substring(5).trim();
            int fromPos = rest.indexOf("/from");
            int toPos = rest.indexOf("/to");
            if (fromPos < 0 || toPos < 0 || toPos < fromPos) {
                throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
            }
            String desc = rest.substring(0, fromPos).trim();
            String from = rest.substring(fromPos + 5, toPos).trim();
            String to = rest.substring(toPos + 3).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
            }
            Task t = new Event(desc, from, to);
            taskList.add(t);
            storageSafeSave(storage, taskList, ui);
            ui.showTaskAdded(t, taskList.size());
            return false;
        }
        if (trimmed.startsWith("find")) {
            String keyword = trimmed.length() > 4 ? trimmed.substring(4).trim() : "";
            if (keyword.isEmpty()) {
                throw new SilvermoonException("Usage: find <keyword>");
            }
            String needle = keyword.toLowerCase();
            java.util.List<Task> matches = new java.util.ArrayList<>();
            for (Task t : taskList.asList()) {
                if (t.getDescription().toLowerCase().contains(needle)) {
                    matches.add(t);
                }
            }
            ui.showMatchingTasks(matches);
            return false;
        }

        throw new SilvermoonException("Sorry, I don't recognize that command.");
    }

    /**
     * Parses a 1-based task index from a command like {@code "mark 2"}.
     *
     * @param input full command line
     * @param cmd   command name for error messages
     * @param size  current number of tasks
     * @return zero-based index into the task list
     * @throws SilvermoonException if the index is missing, non-integer, or out of range
     */
    private static int parseIndex(String input, String cmd, int size) throws SilvermoonException {
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

    /**
     * Saves the current task list; reports an error via {@link Ui} if saving fails.
     *
     * <p>Failures are surfaced to the user rather than thrown to the caller.</p>
     */
    private static void storageSafeSave(Storage storage, TaskList taskList, Ui ui) {
        try {
            storage.save(taskList.asList());
        } catch (Exception e) {
            ui.showError("I couldn't save your tasks just now. Changes are only in memory.");
        }
    }
}

