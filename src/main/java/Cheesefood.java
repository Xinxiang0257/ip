import java.util.Scanner;

public class Cheesefood {
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);

        String[] tasks = new String[100];
        int taskCount = 0;

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Cheesefood");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        String userInput;
        userInput = scanner.nextLine();

        while (!userInput.equals("bye")){
            if (userInput.equals("list")) {
                if (taskCount <= 100) {
                    System.out.println(horizontalLine);
                    for (int i = 0; i < taskCount; i++) {
                        System.out.println(" " + (i + 1) + ". " + tasks[i]);
                    }
                    System.out.println(horizontalLine);
                }
            } else {
                if (taskCount < 100) {
                    tasks[taskCount] = userInput;
                    System.out.println(horizontalLine);
                    System.out.println(" added: " + userInput);
                    System.out.println(horizontalLine);
                    taskCount++;
                }
            }
            userInput = scanner.nextLine();
        }

        System.out.println(horizontalLine);
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);

        scanner.close();
    }
}