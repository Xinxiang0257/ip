package duke;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;


public class Storage {
    private final String filePath;

    public Storage(String filePath) {
        this.filePath = filePath;
    }

    public void loadTasksFromFile(ArrayList<Task> tasks) throws CheesefoodException {

        try {
            File file = new File(filePath);

            // Create the file if it doesn't exist
            if (!file.exists()) {
                file.createNewFile();
            }

            java.util.List<String> lines = Files.readAllLines(Paths.get(filePath));

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

    private static Task createTask(String[] taskLine) throws CheesefoodException {

        String type = taskLine[0].trim();
        boolean isDone = taskLine[1].trim().equals("1");
        String description = taskLine[2].trim();

        Task task = null;

        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (taskLine.length >= 4) {
                    String by = taskLine[3].trim();
                    try {
                        task = new Deadline(description, by);
                    } catch (CheesefoodException e) {
                        System.out.println("Error parsing event dates: " + e.getMessage());
                    }
                }
                break;
            case "E":
                if (taskLine.length >= 5) {
                    String from = taskLine[3].trim();
                    String to = taskLine[4].trim();
                    try {
                        task = new Event(description, from, to);
                    } catch (CheesefoodException e) {
                        System.out.println("Error parsing event dates: " + e.getMessage());
                    }
                }
                break;
        }

        if (isDone) {
            task.markAsDone();
        }
        return task;
    }

    public void saveTasksToFile(ArrayList<Task> tasks) {
        try {

            FileWriter writer = new FileWriter(filePath);
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
//
}