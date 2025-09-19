package duke;

public class Ui {
    public static final String HORIZONTAL_LINE = "____________________________________________________________";

    public static void showWelcome() {
        System.out.println(HORIZONTAL_LINE);
        System.out.println(" Hello! I'm duke.Cheesefood");
        System.out.println(" What can I do for you?");
        System.out.println(HORIZONTAL_LINE);
    }

    public static void showGoodbye() {
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println(HORIZONTAL_LINE);
    }

}