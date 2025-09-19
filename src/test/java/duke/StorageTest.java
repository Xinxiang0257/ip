package duke;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.nio.file.Files;

public class StorageTest {
    @TempDir
    Path tempDir;
    private Storage storage;
    private ArrayList<Task> tasks;

    @BeforeEach
    public void setUp() {
        tasks = new ArrayList<>();
    }

    @Test
    public void testLoadTasksFromFile_emptyFile() throws CheesefoodException, IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "".getBytes());

        storage = new Storage(testFile.toString());
        storage.loadTasksFromFile(tasks);

        assertEquals(0, tasks.size());
    }

    @Test
    public void testLoadTasksFromFile_todoTask() throws CheesefoodException, IOException {
        Path testFile = tempDir.resolve("test.txt");
        Files.write(testFile, "T | 0 | Read book".getBytes());

        storage = new Storage(testFile.toString());
        storage.loadTasksFromFile(tasks);

        assertEquals(1, tasks.size());
        assertInstanceOf(Todo.class, tasks.get(0));
        assertEquals("[T][ ] Read book", tasks.get(0).toString());
    }

    @Test
    public void testLoadTasksFromFile_mixedTasks() throws CheesefoodException, IOException {
        Path testFile = tempDir.resolve("test.txt");
        String fileContent = "T | 1 | Completed todo\n" +
                "D | 0 | Submit assignment | 2023-12-31\n" +
                "E | 0 | Team meeting | 2023-12-31 | 2024-01-01";
        Files.write(testFile, fileContent.getBytes());

        storage = new Storage(testFile.toString());
        storage.loadTasksFromFile(tasks);

        assertEquals(3, tasks.size());

        // Check first task (Todo)
        assertInstanceOf(Todo.class, tasks.get(0));
        assertTrue(tasks.get(0).isDone());
        assertEquals("[T][X] Completed todo", tasks.get(0).toString());

        // Check second task (Deadline)
        assertInstanceOf(Deadline.class, tasks.get(1));
        assertFalse(tasks.get(1).isDone());

        // Check third task (Event)
        assertInstanceOf(Event.class, tasks.get(2));
        assertFalse(tasks.get(2).isDone());
    }
}