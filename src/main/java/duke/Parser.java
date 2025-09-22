package duke;

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private String input;
    public static Scanner scanner;
    public Storage data;
    public static ArrayList<Task> tasks = new ArrayList<>();

    public Parser(String userInput, Storage data, Scanner scanner) throws CheesefoodException {
        this.input = userInput;
        this.data = data;
        this.scanner = scanner;
    }
    public String parse() throws CheesefoodException {
        String output = "";

        try {
            if (this.input.equals("list")) {
                output = listTasks();
            } else if (this.input.startsWith("mark")) {
                output = markTask(this.input);
            } else if (this.input.startsWith("unmark")) {
                output = unmarkTask(this.input);
            } else if (this.input.startsWith("todo")) {
                output = addTodo(this.input);
            } else if (this.input.startsWith("deadline")) {
                output = addDeadline(this.input);
            } else if (this.input.startsWith("event")) {
                output = addEvent(this.input);
            } else if (this.input.startsWith("delete")) {
                output = deleteTask(this.input);
            } else if (this.input.equals("bye")) {
                output = Ui.showGoodbye();
            } else {
                throw new CheesefoodException("Invalid instruction");
            }
        } catch (CheesefoodException e) {
            output = "Error: " + e.getMessage();
        }

        data.saveTasksToFile(tasks);
        return output;
    }


    private static String listTasks() {
        if (tasks.isEmpty()) {
            return " Your task list is empty";
        } else {
            StringBuilder result = new StringBuilder(" Here are your tasks:");
            for (int i = 0; i < tasks.size(); i++) {
                result.append("\n ").append(i + 1).append(".").append(tasks.get(i));
            }
            return result.toString();
        }
    }

    private static String markTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                tasks.get(taskNumber).markAsDone();
                return " Marked as done:\n   " + tasks.get(taskNumber);
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String unmarkTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                tasks.get(taskNumber).markAsNotDone();
                return " Marked as not done:\n   " + tasks.get(taskNumber);
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String deleteTask(String command) throws CheesefoodException {
        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                Task removedTask = tasks.get(taskNumber);
                tasks.remove(taskNumber);
                return " Noted. I've removed this task:\n   " + removedTask + "\n Now you have " + tasks.size() + " tasks in the list.";
            } else {
                throw new CheesefoodException("Insufficient information");
            }
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String addTodo(String command) throws CheesefoodException {
        String description = command.substring(5).trim();

        if (description.isEmpty()) {
            throw new CheesefoodException("Insufficient information");
        }

        Todo newTodo = new Todo(description);
        tasks.add(newTodo);
        return " Added todo:\n   " + newTodo + "\n Total tasks: " + tasks.size();
    }

    private static String addDeadline(String command) throws CheesefoodException {
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
            return " Added deadline:\n   " + newDeadline + "\n Total tasks: " + tasks.size();
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String addEvent(String command) throws CheesefoodException {
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
            return " Added event:\n   " + newEvent + "\n Total tasks: " + tasks.size();
        } catch (Exception e) {
            throw new CheesefoodException("Insufficient information");
        }
    }
}