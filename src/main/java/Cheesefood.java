import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A chatbot named Cheesefood that helps users manage their tasks.
 * Supports adding tasks, listing tasks, marking tasks as done/not done,
 * and provides a simple command-line interface for user interaction.
 */
public class Cheesefood {
    public static String horizontalLine = "____________________________________________________________";
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Task> tasks = new ArrayList<>();
    public static String currentDir = System.getProperty("user.dir");
    public static String DATA_FILE_PATH =  currentDir + "/data.txt";

    /**
     * Entry point of the Cheesefood chatbot application.
     * Displays welcome message and starts processing user commands.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Cheesefood");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        // Load tasks from file when starting up
        loadTasksFromFile();

        processCommand();
    }

    private static void processCommand() {
        String userInput;
        userInput = scanner.nextLine();

        System.out.println(horizontalLine);

        while (!userInput.equals("bye")){
            try {
                if (userInput.equals("list")) {
                    listTasks();
                } else if (userInput.startsWith("mark")) {
                    markTask(userInput);
                } else if (userInput.startsWith("unmark")) {
                    unmarkTask(userInput);
                } else if (userInput.startsWith("todo")) {
                    addTodo(userInput);
                } else if (userInput.startsWith("deadline")) {
                    addDeadline(userInput);
                } else if (userInput.startsWith("event")){
                    addEvent(userInput);
                } else if (userInput.startsWith("delete")) {
                    deleteTask(userInput);
                } else {
                    throw new CheesefoodException("Invalid instruction");
                }
            } catch (CheesefoodException e) {
                System.out.println(" Error: " + e.getMessage());
            }

            saveTasksToFile();
            System.out.println(horizontalLine);
            userInput = scanner.nextLine();
        }

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }

    /**
     * Loads tasks from the file when the application starts.
     * Creates the file and directory if they don't exist.
     */
    private static void loadTasksFromFile() {

        try {
            File file = new File(DATA_FILE_PATH);

            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            // Read all lines from the file
            java.util.List<String> lines = Files.readAllLines(Paths.get(DATA_FILE_PATH));

            for (String line : lines) {
                if (line.trim().isEmpty()) {
                    continue; // ignore empty lines
                }

                String[] parts = line.split(" \\| ");

                Task task = createTask(parts);

                if (task != null) {
                    tasks.add(task);
                }
            }

        } catch (IOException e) {
            System.out.println(" Error loading tasks from file: " + e.getMessage());
        }
    }

    private static Task createTask(String[] taskLine) {

        String type = taskLine[0].trim();
        boolean isDone = taskLine[1].trim().equals("1"); // change the name to isMarked
        String description = taskLine[2].trim();

        Task task = null;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (taskLine.length >= 4) {
                    String by = taskLine[3].trim();
                    task = new Deadline(description, by);
                }
                break;
            case "E":
                if (taskLine.length >= 5) {
                    String from = taskLine[3].trim();
                    String to = taskLine[4].trim();
                    task = new Event(description, from, to);
                }
                break;
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;

    }

    /**
     * Saves all tasks to the file in the specified format.
     */
    private static void saveTasksToFile() {
        try {
            File directory = new File(DATA_FILE_PATH).getParentFile();
            if (!directory.exists()) {
                directory.mkdirs();
            }

            FileWriter writer = new FileWriter(DATA_FILE_PATH);

            for (Task task : tasks) {
                String line;

                if (task instanceof Todo) {
                    line = String.format("T | %d | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription());
                } else if (task instanceof Deadline) {
                    Deadline deadline = (Deadline) task;
                    line = String.format("D | %d | %s | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription(),
                            deadline.getBy());
                } else if (task instanceof Event) {
                    Event event = (Event) task;
                    line = String.format("E | %d | %s | %s | %s",
                            task.isDone() ? 1 : 0,
                            task.getDescription(),
                            event.getFrom(),
                            event.getTo());
                } else {
                    continue;
                }

                writer.write(line + System.lineSeparator());
            }

            writer.close();

        } catch (IOException e) {
            System.out.println(" Error saving tasks to file: " + e.getMessage());
        }
    }

    private static void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println(" Your task list is empty");
        } else {
            System.out.println(" Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    private static void markTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                tasks.get(taskNumber).markAsDone();
                System.out.println(" Marked as done:");
                System.out.println("   " + tasks.get(taskNumber));
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static void unmarkTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                tasks.get(taskNumber).markAsNotDone();
                System.out.println(" Marked as not done:");
                System.out.println("   " + tasks.get(taskNumber));
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static void deleteTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                Task removedTask = tasks.get(taskNumber);
                tasks.remove(taskNumber);
                System.out.println(" Noted. I've removed this task:");
                System.out.println("   " + removedTask);
                System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static void addTodo(String command) throws CheesefoodException {
        String description = command.substring(5).trim();

        if (description.isEmpty()) {
            throw new CheesefoodException("Insufficient information");
        }

        Todo newTodo = new Todo(description);
        tasks.add(newTodo);
        System.out.println(" Added todo:");
        System.out.println("   " + newTodo);
        System.out.println(" Total tasks: " + tasks.size());
    }

    private static void addDeadline(String command) throws CheesefoodException {
        try {
            String remaining = command.substring(9).trim();
            String[] parts = remaining.split(" /by ", 2);

            if (parts.length < 2) {
                throw new CheesefoodException("Insufficient information");
            }

            String description = parts[0].trim();
            String by = parts[1].trim();

            if (description.isEmpty() || by.isEmpty()) {
                throw new CheesefoodException("Insufficient information");
            }

            Deadline newDeadline = new Deadline(description, by);
            tasks.add(newDeadline);
            System.out.println(" Added deadline:");
            System.out.println("   " + newDeadline);
            System.out.println(" Total tasks: " + tasks.size());
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static void addEvent(String command) throws CheesefoodException {
        try {
            String remaining = command.substring(6).trim();
            String[] parts = remaining.split(" /from ", 2);

            if (parts.length < 2) {
                throw new CheesefoodException("Insufficient information");
            }

            String description = parts[0].trim();
            String remainingTime = parts[1].trim();
            String[] timeParts = remainingTime.split(" /to ", 2);

            if (timeParts.length < 2) {
                throw new CheesefoodException("Insufficient information");
            }

            String from = timeParts[0].trim();
            String to = timeParts[1].trim();

            if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
                throw new CheesefoodException("Insufficient information");
            }

            Event newEvent = new Event(description, from, to);
            tasks.add(newEvent);
            System.out.println(" Added event:");
            System.out.println("   " + newEvent);
            System.out.println(" Total tasks: " + tasks.size());
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }
}