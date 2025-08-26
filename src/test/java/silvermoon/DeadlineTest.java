package silvermoon;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class DeadlineTest {
    @Test
    void toString_formatsDateAndStatus() {
        Deadline d = new Deadline("return book", LocalDate.parse("2019-12-02"));
        // initial status
        String s1 = d.toString();
        assertTrue(s1.startsWith("[D][ ]"), "Initial status should be not done");
        assertTrue(s1.contains("(by: Dec 2 2019)"), "Date should print as 'Dec 2 2019'");

        // after marking done
        d.markAsDone();
        String s2 = d.toString();
        assertTrue(s2.startsWith("[D][X]"), "After mark, status should be done");
    }
}

