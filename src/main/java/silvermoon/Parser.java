package silvermoon;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user input strings and executes actions based on it.
 */
public class Parser {

    /**
     * Parses and executes a single user input.
     *
     * @param input    raw line from the user
     * @param taskList list to operate on
     * @param ui       UI for user-visible output
     * @param storage  storage for changes
     * @return {@code true} if the caller exit; {@code false} otherwise
     * @throws SilvermoonException if command is recognised but incorrect
     */
    public static boolean parseAndExecute(String input, TaskList taskList, Ui ui, Storage storage)
            throws SilvermoonException {
        String trimmed = input.trim();
        if (trimmed.isEmpty()) return false;

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
            if (rest.isEmpty()) throw new SilvermoonException("The description of a todo cannot be empty.");
            Task t = new ToDo(rest);
            taskList.add(t);
            storageSafeSave(storage, taskList, ui);
            ui.showTaskAdded(t, taskList.size());
            return false;
        }
        if (trimmed.startsWith("deadline")) {
            String rest = trimmed.substring(8).trim();
            int p = rest.indexOf("/by");
            if (p < 0) throw new SilvermoonException("Usage: deadline <description> /by <yyyy-MM-dd>");
            String desc = rest.substring(0, p).trim();
            String byRaw = rest.substring(p + 3).trim();
            if (desc.isEmpty() || byRaw.isEmpty())
                throw new SilvermoonException("Usage: deadline <description> /by <yyyy-MM-dd>");
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
            int pf = rest.indexOf("/from");
            int pt = rest.indexOf("/to");
            if (pf < 0 || pt < 0 || pt < pf)
                throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
            String desc = rest.substring(0, pf).trim();
            String from = rest.substring(pf + 5, pt).trim();
            String to = rest.substring(pt + 3).trim();
            if (desc.isEmpty() || from.isEmpty() || to.isEmpty())
                throw new SilvermoonException("Usage: event <description> /from <start> /to <end>");
            Task t = new Event(desc, from, to);
            taskList.add(t);
            storageSafeSave(storage, taskList, ui);
            ui.showTaskAdded(t, taskList.size());
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
        if (parts.length != 2) throw new SilvermoonException("Usage: " + cmd + " <taskNumber>");
        int oneBased;
        try { oneBased = Integer.parseInt(parts[1]); }
        catch (NumberFormatException e) { throw new SilvermoonException("Task number must be an integer."); }
        int idx = oneBased - 1;
        if (idx < 0 || idx >= size)
            throw new SilvermoonException("Task number must be between 1 and " + size + ".");
        return idx;
    }

    private static void storageSafeSave(Storage storage, TaskList taskList, Ui ui) {
        try {
            storage.save(taskList.asList());
        } catch (Exception e) {
            ui.showError("I couldn't save your tasks just now. Changes are only in memory.");
        }
    }
}
