package duke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class ParserTest {

    private Storage storage;
    private Scanner scanner;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {

        storage = new Storage("test.txt");
        scanner = new Scanner(System.in);
        Parser.tasks.clear();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testParse_validTodoCommand_returnsSuccessMessage() throws CheesefoodException {
        Parser parser = new Parser("todo Read book", storage, scanner);
        String result = parser.parse();

        assertTrue(result.contains("Added todo"));
        assertTrue(result.contains("Read book"));
        assertTrue(result.contains("Total tasks: 1"));
        assertEquals(1, Parser.tasks.size());
    }

    @Test
    public void testParse_invalidCommand_returnsErrorMessage() throws CheesefoodException {
        Parser parser = new Parser("invalid command", storage, scanner);
        String result = parser.parse();

        assertTrue(result.contains("Error: Invalid instruction"));
        assertTrue(result.startsWith("Error:"));
    }

    @Test
    public void testParse_listCommandWithNoTasks_returnsEmptyList() throws CheesefoodException {
        Parser parser = new Parser("list", storage, scanner);
        String result = parser.parse();

        assertTrue(result.contains("Here are your tasks:"));
        assertFalse(result.contains("1."));
    }

    @Test
    public void testParse_markCommandWithInvalidNumber_returnsErrorMessage() throws CheesefoodException {
        Parser addParser = new Parser("todo Test task", storage, scanner);
        addParser.parse();

        Parser markParser = new Parser("mark 5", storage, scanner);
        String result = markParser.parse();

        assertTrue(result.contains("Error: Invalid task number"));
        assertTrue(result.contains("Please provide a number between 1 and 1"));
    }
}