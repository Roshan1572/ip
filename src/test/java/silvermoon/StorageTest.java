package silvermoon;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StorageTest {
    @TempDir Path tmp;

    @Test
    void saveAndLoad_roundTripPreservesTasks() throws Exception {
        // Run Storage against a temp project root by overriding user.dir
        String orig = System.getProperty("user.dir");
        try {
            System.setProperty("user.dir", tmp.toString());

            Storage storage = new Storage("test-tasks.txt");
            TaskList list = new TaskList();

            ToDo t1 = new ToDo("read book");
            t1.markAsDone();
            Deadline t2 = new Deadline("return book", LocalDate.parse("2019-10-15"));
            Event t3 = new Event("project meeting", "Mon 2pm", "4pm");

            list.add(t1);
            list.add(t2);
            list.add(t3);

            storage.save(list.asList());

            // Load back
            Storage storage2 = new Storage("test-tasks.txt");
            List<Task> loaded = storage2.load();

            assertEquals(3, loaded.size());

            assertTrue(loaded.get(0) instanceof ToDo);
            assertEquals("read book", loaded.get(0).description);
            assertTrue(loaded.get(0).isDone);

            assertTrue(loaded.get(1) instanceof Deadline);
            Deadline ld = (Deadline) loaded.get(1);
            assertEquals("return book", ld.description);
            assertEquals(LocalDate.parse("2019-10-15"), ld.getBy());
            assertFalse(ld.isDone);

            assertTrue(loaded.get(2) instanceof Event);
            Event le = (Event) loaded.get(2);
            assertEquals("project meeting", le.description);
            assertEquals("Mon 2pm", le.from);
            assertEquals("4pm", le.to);
        } finally {
            System.setProperty("user.dir", orig);
        }
    }
}
