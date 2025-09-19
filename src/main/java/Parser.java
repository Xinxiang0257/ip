import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private String input;
    public static Scanner scanner = new Scanner(System.in);
    public Storage data;
    public static ArrayList<Task> tasks = new ArrayList<>();

    public Parser(String userInput, Storage data) throws CheesefoodException {
        this.input = userInput;
        this.data = data;
    }

    public void parse() throws CheesefoodException {

        data.loadTasksFromFile(tasks);

        System.out.println(Ui.HORIZONTAL_LINE);
        while (!this.input.equals("bye")){
            try {
                if (this.input.equals("list")) {
                    listTasks();
                    markTask(this.input);
                } else if (this.input.startsWith("unmark")) {
                    unmarkTask(this.input);
                } else if (this.input.startsWith("todo")) {
                    addTodo(this.input);
                } else if (this.input.startsWith("deadline")) {
                    addDeadline(this.input);
                } else if (this.input.startsWith("event")){
                    addEvent(this.input);
                } else if (this.input.startsWith("delete")) {
                    deleteTask(this.input);
                } else {
                    throw new CheesefoodException("Invalid instruction");
                }
            } catch (CheesefoodException e) {
                System.out.println(" Error: " + e.getMessage());
            }

            data.saveTasksToFile(tasks);

            System.out.println(Ui.HORIZONTAL_LINE);
            this.input = scanner.nextLine();
        }
        Ui.showGoodbye();
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