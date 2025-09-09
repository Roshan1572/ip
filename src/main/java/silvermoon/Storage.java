package silvermoon;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

/**
 * Sends tasks to {@code ./data/<fileName>} and loads them back on startup.
 * Creates the {@code data/} directory and file on first use if missing.
 */
public class Storage {
    private final Path dataDir;
    private final Path dataFile;

    public Storage(String fileName) {
        assert fileName != null && !fileName.isBlank() : "fileName must not be blank";
        // Robust: if tests run from text-ui-test/, resolve to project root
        Path base = Paths.get(System.getProperty("user.dir"));
        if (base.getFileName() != null && base.getFileName().toString().equals("text-ui-test")) {
            base = base.getParent(); // project root
        }
        this.dataDir = base.resolve("data");
        this.dataFile = dataDir.resolve(fileName);
    }
    /** Loads tasks from disk; returns an empty list if the file does not exist. */
    public List<Task> load() throws IOException {
        if (!Files.exists(dataFile)) {
            // First run: ensure folder exists, but return empty list
            Files.createDirectories(dataDir);
            return new ArrayList<>();
        }
        List<String> lines = Files.readAllLines(dataFile);
        List<Task> tasks = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            Task t = parseLine(line);
            if (t != null) tasks.add(t);
        }
        return tasks;
    }

    /** Overwrites the file with the given tasks. */
    public void save(List<Task> tasks) throws IOException {
        List<String> lines = new ArrayList<>();
        for (Task t : tasks) {
            lines.add(serialize(t));
        }
        Files.createDirectories(dataDir);
        Files.write(dataFile, lines, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private Task parseLine(String line) {
        // format:
        // T | 1 | description
        // D | 0 | description | by
        // E | 1 | description | from | to
        assert line != null : "Line must not be null";
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length < 3) return null;
        String type = parts[0];
        boolean done = "1".equals(parts[1]);
        String desc = parts[2];

        Task t;
        switch (type) {
        case "T":
            t = new ToDo(desc);
            break;
        case "D":
            String byIso = parts.length >= 4 ? parts[3] : "";
            LocalDate byDate = byIso.isEmpty() ? LocalDate.now() : LocalDate.parse(byIso);
            t = new Deadline(desc, byDate);
            break;
        case "E":
            String from = parts.length >= 4 ? parts[3] : "";
            String to = parts.length >= 5 ? parts[4] : "";
            t = new Event(desc, from, to);
            break;
        default:
            return null;
        }
        if (done) t.markAsDone();
        return t;
    }

    private String serialize(Task t) {
        assert t != null : "Cannot serialize null task";
        String done = t.isDone ? "1" : "0";
        if (t instanceof ToDo) {
            return String.join(" | ", "T", done, t.description);
        } else if (t instanceof Deadline) {
            Deadline d = (Deadline) t;
            return String.join(" | ", "D", done, d.description, d.getBy().toString()); // yyyy-MM-dd
        } else if (t instanceof Event) {
            Event e = (Event) t;
            return String.join(" | ", "E", done, e.description, e.from, e.to);
        } else {
            // fallback
            return String.join(" | ", "T", done, t.description);
        }
    }
}
