/**
 * Represents exceptions specific to the Cheesefood chatbot.
 * Used to handle errors related to invalid user commands.
 */
public class CheesefoodException extends Exception {
    /**
     * Constructs a new CheesefoodException with the specified error message.
     *
     * @param message The detail message explaining the error.
     */
    public CheesefoodException(String message) {
        super(message);
    }
}