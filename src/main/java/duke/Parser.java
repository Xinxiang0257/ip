package duke;

import java.util.ArrayList;
import java.util.Scanner;

public class Parser {
    private String input;
    public static Scanner scanner;
    public Storage data;
    public static ArrayList<Task> tasks = new ArrayList<>();

    public Parser(String userInput, Storage data, Scanner scanner) throws CheesefoodException {
        assert userInput != null : "User input cannot be null";
        assert data != null : "Storage cannot be null";

        this.input = userInput;
        this.data = data;
        this.scanner = scanner; // may not be applicable
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
            } else if (this.input.startsWith("find")) {
                output = findTasks(this.input);
            } else {
                throw new CheesefoodException("Invalid instruction");
            }
        } catch (CheesefoodException e) {
            output = "Error: " + e.getMessage();
        }

        assert output != null : "Parse method should always return a non-null string";
        data.saveTasksToFile(tasks);
        return output;
    }

    private static String listTasks() {
        assert tasks != null : "Tasks list should be initialized";

        StringBuilder result = new StringBuilder(" Here are your tasks:");
        for (int i = 0; i < tasks.size(); i++) {
            result.append("\n ").append(i + 1).append(".").append(tasks.get(i));
        }
        return result.toString();
    }

    private static String markTask(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("mark") : "Method should only be called with mark commands";

        int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1;
        if (taskNumber >= 0 && taskNumber < tasks.size()) {
            Task task = tasks.get(taskNumber);
            assert task != null : "Retrieved task should not be null";

            task.markAsDone();
            return " Marked as done:\n   " + task;
        } else {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String unmarkTask(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("unmark") : "Method should only be called with unmark commands";

        int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
        if (taskNumber >= 0 && taskNumber < tasks.size()) {
            Task task = tasks.get(taskNumber);
            assert task != null : "Retrieved task should not be null";

            task.markAsNotDone();
            return " Marked as not done:\n   " + task;
        } else {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String deleteTask(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("delete") : "Method should only be called with delete commands";

        int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
        if (taskNumber >= 0 && taskNumber < tasks.size()) {
            int initialSize = tasks.size();
            Task removedTask = tasks.get(taskNumber);
            assert removedTask != null : "Task to remove should not be null";

            tasks.remove(taskNumber);
            assert tasks.size() == initialSize - 1 : "Task list size should decrease by 1 after deletion";

            return " Noted. I've removed this task:\n   " + removedTask + "\n Now you have " + tasks.size() + " tasks in the list.";
        } else {
            throw new CheesefoodException("Insufficient information");
        }
    }

    private static String addTodo(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("todo") : "Method should only be called with todo commands";

        String description = command.substring(5).trim();

        if (description.isEmpty()) {
            throw new CheesefoodException("Insufficient information");
        }

        int initialSize = tasks.size();
        Todo newTodo = new Todo(description);
        tasks.add(newTodo);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding todo";
        assert tasks.contains(newTodo) : "Newly added todo should be in the task list";

        return " Added todo:\n   " + newTodo + "\n Total tasks: " + tasks.size();
    }

    private static String addDeadline(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("deadline") : "Method should only be called with deadline commands";

        String remaining = command.substring(9).trim();
        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            if (parts[0].isEmpty()) {
                throw new CheesefoodException("Lack of task description. Please use the format deadline [description] /by [YYYY-MM-DD]");
            } else {
                throw new CheesefoodException("Lack of due date. Please use the format deadline [description] /by [YYYY-MM-DD]");
            }
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        int initialSize = tasks.size();
        Deadline newDeadline = new Deadline(description, by);
        tasks.add(newDeadline);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding deadline";

        return " Added deadline:\n   " + newDeadline + "\n Total tasks: " + tasks.size();
    }

    private static String addEvent(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("event") : "Method should only be called with event commands";

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

        int initialSize = tasks.size();
        Event newEvent = new Event(description, from, to);
        tasks.add(newEvent);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding event";

        return " Added event:\n   " + newEvent + "\n Total tasks: " + tasks.size();
    }

    private static String findTasks(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("find") : "Method should only be called with find commands";

        try {
            String keyword = command.substring(5).trim();

            if (keyword.isEmpty()) {
                throw new CheesefoodException("Please provide a keyword to search for. eg. find book");
            }

            ArrayList<Task> matchingTasks = new ArrayList<>();

            for (Task task : tasks) {
                assert task != null : "Task in list should not be null";
                if (task.getDescription().toLowerCase().contains(keyword.toLowerCase())) {
                    matchingTasks.add(task);
                }
            }

            if (matchingTasks.isEmpty()) {
                return " No tasks found containing: " + keyword;
            } else {
                StringBuilder result = new StringBuilder(" Here are the matching tasks in your list:");
                for (int i = 0; i < matchingTasks.size(); i++) {
                    result.append("\n ").append(i + 1).append(".").append(matchingTasks.get(i));
                }
                return result.toString();
            }

        } catch (Exception e) {
            throw new CheesefoodException("Invalid find command format. Use: find [keyword]");
        }
    }
}