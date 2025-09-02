import java.util.Scanner;
import java.util.ArrayList;

/**
 * A chatbot named Cheesefood that helps users manage their tasks.
 * Supports adding tasks, listing tasks, marking tasks as done/not done,
 * and provides a simple command-line interface for user interaction.
 */
public class Cheesefood {
    public static String horizontalLine = "____________________________________________________________";
    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<Task> tasks = new ArrayList<>();

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

        processCommand();
    }

    private static void processCommand() {
        String userInput;
        userInput = scanner.nextLine();

        while (!userInput.equals("bye")){
            if (userInput.equals("list")) {
                System.out.println(horizontalLine);
                listTasks();
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("mark")) {
                System.out.println(horizontalLine);
                markTask(userInput);
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("unmark")) {
                System.out.println(horizontalLine);
                unmarkTask(userInput);
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("todo")) {
                System.out.println(horizontalLine);
                addTodo(userInput);
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("deadline")) {
                System.out.println(horizontalLine);
                addDeadline(userInput);
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("event")){
                System.out.println(horizontalLine);
                addEvent(userInput);
                System.out.println(horizontalLine);
            } else { }

            userInput = scanner.nextLine();
        }
        System.out.println(horizontalLine);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }

    private static void listTasks() {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(" " + (i + 1) + "." + tasks.get(i));
        }
    }

    private static void markTask(String command) {
        int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1;
        if (taskNumber >= 0 && taskNumber < tasks.size()) {
            tasks.get(taskNumber).markAsDone();
            System.out.println(" Nice! I've marked this task as done:");
            System.out.println("   " + tasks.get(taskNumber));
        }
    }

    private static void unmarkTask(String command) {
        int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
        if (taskNumber >= 0 && taskNumber < tasks.size()) {
            tasks.get(taskNumber).markAsNotDone();
            System.out.println(" OK, I've marked this task as not done yet:");
            System.out.println("   " + tasks.get(taskNumber));
        }
    }

    private static void addTodo(String command) {
        String description = command.substring(5).trim();

        Todo newTodo = new Todo(description);
        tasks.add(newTodo);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newTodo);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addDeadline(String command) {
        String remaining = command.substring(9).trim();

        String[] parts = remaining.split(" /by ", 2);

        String description = parts[0].trim();
        String by = parts[1].trim();

        Deadline newDeadline = new Deadline(description, by);
        tasks.add(newDeadline);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newDeadline);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }

    private static void addEvent(String command) {
        String remaining = command.substring(6).trim();

        String[] parts = remaining.split(" /from ", 2);

        String description = parts[0].trim();
        String remainingTime = parts[1].trim();

        String[] timeParts = remainingTime.split(" /to ", 2);

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        Event newEvent = new Event(description, from, to);
        tasks.add(newEvent);
        System.out.println(" Got it. I've added this task:");
        System.out.println("   " + newEvent);
        System.out.println(" Now you have " + tasks.size() + " tasks in the list.");
    }
}