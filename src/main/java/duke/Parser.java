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

        if (command.substring(4).trim().isEmpty()) {
            throw new CheesefoodException("Please specify a task number. Usage: mark [task number]");
        }

        try {
            int taskNumber = Integer.parseInt(command.substring(5).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                Task task = tasks.get(taskNumber);
                assert task != null : "Retrieved task should not be null";

                task.markAsDone();
                return " Marked as done:\n   " + task;
            } else {
                throw new CheesefoodException("Invalid task number. Please provide a number between 1 and " + tasks.size());
            }
        } catch (NumberFormatException e) {
            throw new CheesefoodException("Please provide a valid task number. Usage: mark [task number]");
        }
    }

    private static String unmarkTask(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("unmark") : "Method should only be called with unmark commands";

        if (command.substring(6).trim().isEmpty()) {
            throw new CheesefoodException("Please specify a task number. Usage: unmark [task number]");
        }

        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                Task task = tasks.get(taskNumber);
                assert task != null : "Retrieved task should not be null";

                task.markAsNotDone();
                return " Marked as not done:\n   " + task;
            } else {
                throw new CheesefoodException("Invalid task number. Please provide a number between 1 and " + tasks.size());
            }
        } catch (NumberFormatException e) {
            throw new CheesefoodException("Please provide a valid task number. Usage: unmark [task number]");
        }
    }

    private static String deleteTask(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("delete") : "Method should only be called with delete commands";

        if (command.substring(6).trim().isEmpty()) {
            throw new CheesefoodException("Please specify a task number. Usage: delete [task number]");
        }

        try {
            int taskNumber = Integer.parseInt(command.substring(7).trim()) - 1;
            if (taskNumber >= 0 && taskNumber < tasks.size()) {
                int initialSize = tasks.size();
                Task removedTask = tasks.get(taskNumber);
                assert removedTask != null : "Task to remove should not be null";

                tasks.remove(taskNumber);
                assert tasks.size() == initialSize - 1 : "Task list size should decrease by 1 after deletion";

                return " Noted. I've removed this task:\n   " + removedTask + "\n Now you have " + tasks.size() + " tasks in the list.";
            } else {
                throw new CheesefoodException("Invalid task number. Please provide a number between 1 and " + tasks.size());
            }
        } catch (NumberFormatException e) {
            throw new CheesefoodException("Please provide a valid task number. Usage: delete [task number]");
        }
    }

    private static String addTodo(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("todo") : "Method should only be called with todo commands";

        if (command.length() <= 4) {
            throw new CheesefoodException("Todo description cannot be empty. Usage: todo [description]");
        }

        String description = command.substring(5).trim();

        if (description.isEmpty()) {
            throw new CheesefoodException("Todo description cannot be empty. Usage: todo [description]");
        }

        int initialSize = tasks.size();
        Todo newTodo = new Todo(description);

        if (isDuplicate(newTodo)) {
            return "Rejected: Task already exists";
        }

        tasks.add(newTodo);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding todo";
        assert tasks.contains(newTodo) : "Newly added todo should be in the task list";

        return " Added todo:\n   " + newTodo + "\n Total tasks: " + tasks.size();
    }

    private static String addDeadline(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("deadline") : "Method should only be called with deadline commands";

        String remaining = command.substring(9).trim();

        if (remaining.isEmpty()) {
            throw new CheesefoodException("Please provide both description and due date. Usage: deadline [description] /by [YYYY-MM-DD]");
        }

        String[] parts = remaining.split(" /by ", 2);

        if (parts.length < 2) {
            String descriptionPart = parts[0].trim();
            if (descriptionPart.isEmpty()) {
                throw new CheesefoodException("Deadline description cannot be empty. Usage: deadline [description] /by [YYYY-MM-DD]");
            } else if (!remaining.contains("/by")) {
                throw new CheesefoodException("Missing '/by' keyword. Usage: deadline [description] /by [YYYY-MM-DD]");
            } else {
                throw new CheesefoodException("Missing due date. Usage: deadline [description] /by [YYYY-MM-DD]");
            }
        }

        String description = parts[0].trim();
        String by = parts[1].trim();

        if (description.isEmpty()) {
            throw new CheesefoodException("Deadline description cannot be empty. Usage: deadline [description] /by [YYYY-MM-DD]");
        }
        if (by.isEmpty()) {
            throw new CheesefoodException("Due date cannot be empty. Usage: deadline [description] /by [YYYY-MM-DD]");
        }

        int initialSize = tasks.size();
        Deadline newDeadline = new Deadline(description, by);

        if (isDuplicate(newDeadline)) {
            return "Rejected: Task already exists";
        }
        tasks.add(newDeadline);
        assert tasks.size() == initialSize + 1 : "Task list size should increase by 1 after adding deadline";

        return " Added deadline:\n   " + newDeadline + "\n Total tasks: " + tasks.size();
    }

    private static String addEvent(String command) throws CheesefoodException {
        assert command != null : "Command cannot be null";
        assert command.startsWith("event") : "Method should only be called with event commands";

        String remaining = command.substring(6).trim();

        if (remaining.isEmpty()) {
            throw new CheesefoodException("Please provide description, start time, and end time. Usage: event [description] /from [start] /to [end]");
        }

        String[] parts = remaining.split(" /from ", 2);

        if (parts.length < 2) {
            String descriptionPart = parts[0].trim();
            if (descriptionPart.isEmpty()) {
                throw new CheesefoodException("Event description cannot be empty. Usage: event [description] /from [start] /to [end]");
            } else if (!remaining.contains("/from")) {
                throw new CheesefoodException("Missing '/from' keyword. Usage: event [description] /from [start] /to [end]");
            } else {
                throw new CheesefoodException("Missing start time. Usage: event [description] /from [start] /to [end]");
            }
        }

        String description = parts[0].trim();
        String remainingTime = parts[1].trim();
        String[] timeParts = remainingTime.split(" /to ", 2);

        if (timeParts.length < 2) {
            if (!remainingTime.contains("/to")) {
                throw new CheesefoodException("Missing '/to' keyword. Usage: event [description] /from [start] /to [end]");
            } else {
                throw new CheesefoodException("Missing end time. Usage: event [description] /from [start] /to [end]");
            }
        }

        String from = timeParts[0].trim();
        String to = timeParts[1].trim();

        int initialSize = tasks.size();
        Event newEvent = new Event(description, from, to);
        if (isDuplicate(newEvent)) {
            return "Rejected: Task already exists";
        }
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

    private static boolean isDuplicate(Task newTask) {

        for (Task task : tasks) {
            if (task.equals(newTask)) {
                return true;
            }
        }
        return false;
    }

}

