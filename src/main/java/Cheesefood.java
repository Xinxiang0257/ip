import java.util.Scanner;

public class Cheesefood {
    public static void main(String[] args) {
        String horizontalLine = "____________________________________________________________";
        Scanner scanner = new Scanner(System.in);

        System.out.println(horizontalLine);
        System.out.println(" Hello! I'm Cheesefood");
        System.out.println(" What can I do for you?");
        System.out.println(horizontalLine);

        String userInput;
        do {
            userInput = scanner.nextLine();
            System.out.println(horizontalLine);
            System.out.println(" " + userInput);
            System.out.println(horizontalLine);
        } while (!userInput.equals("bye"));

        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(horizontalLine);

        scanner.close();
    }
}