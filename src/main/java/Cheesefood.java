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
     * Main entry point of the Cheesefood chatbot application.
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
            } else if (userInput.startsWith("mark ")) {
                System.out.println(horizontalLine);
                markTask(userInput);
                System.out.println(horizontalLine);
            } else if (userInput.startsWith("unmark ")) {
                System.out.println(horizontalLine);
                unmarkTask(userInput);
                System.out.println(horizontalLine);
            } else {
                System.out.println(horizontalLine);
                addTask(userInput);
                System.out.println(horizontalLine);
            }
            userInput = scanner.nextLine();
        }
        System.out.println(horizontalLine);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);
    }

    private static void listTasks() {
        if (!tasks.isEmpty()) {
            System.out.println(" Here are the tasks in your list:");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i));
            }
        }
    }

    private static void markTask(String command) {
        int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1; // get the last word of the user's entry
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

    private static void addTask(String command) {
        if (!command.trim().isEmpty()) {
            Task newTask = new Task(command);
            tasks.add(newTask);
            System.out.println(" added: " + command);
        }
    }
}