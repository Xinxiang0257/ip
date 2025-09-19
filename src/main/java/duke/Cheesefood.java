package duke;

import java.util.Scanner;

/**
 * A chatbot named duke.Cheesefood that helps users manage their tasks.
 * Supports adding tasks, listing tasks, marking tasks as done/not done,
 * and provides a simple command-line interface for user interaction.
 */
public class Cheesefood {
    public static String horizontalLine = "____________________________________________________________";
    public static Scanner scanner = new Scanner(System.in);
    //    public static ArrayList<duke.Task> tasks = new ArrayList<>();
    public static String currentDir = System.getProperty("user.dir");
    public static String DATA_FILE_PATH = currentDir + "/data.txt";
    public static Storage data = new Storage(DATA_FILE_PATH);

    /**
     * Entry point of the duke.Cheesefood chatbot application.
     * Displays welcome message and starts processing user commands.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) throws CheesefoodException {

        Ui.showWelcome();

        Storage data = new Storage(DATA_FILE_PATH);

        String userInput;
        userInput = scanner.nextLine();

        Parser inputParser = new Parser(userInput, data);
        inputParser.parse();

    }

}
