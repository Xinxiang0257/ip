import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Class for parsing and formatting dates and times
 */
public class DateTimeParser {

    /**
     * Parses various date-time string formats into LocalDateTime
     * Supports:
     * - yyyy-mm-dd
     * - yyyy-mm-dd HHmm
     * - dd/mm/yyyy
     * - dd/mm/yyyy HHmm
     * - and other common formats
     */
    public static LocalDateTime parse(String dateTimeStr) throws CheesefoodException {
        try {
            if (dateTimeStr.matches("\\d{4}-\\d{1,2}-\\d{1,2}")) { // eg. "2025-01-01"
                return LocalDateTime.parse(dateTimeStr + "T00:00:00");
            } else if (dateTimeStr.matches("\\d{4}-\\d{1,2}-\\d{1,2} \\d{4}")) { // eg. "2025-01-01 2359"
                String[] parts = dateTimeStr.split(" ");
                return LocalDateTime.parse(parts[0] + "T" +
                        parts[1].substring(0, 2) + ":" + parts[1].substring(2, 4) + ":00");
            } else if (dateTimeStr.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) { // eg. 01/01/2025 2359
                String[] parts = dateTimeStr.split(" ");
                String[] dateParts = parts[0].split("/");
                String formattedDate = String.format("%s-%02d-%02d",
                        dateParts[2], Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                return LocalDateTime.parse(formattedDate + "T" +
                        parts[1].substring(0, 2) + ":" + parts[1].substring(2, 4) + ":00");
            } else if (dateTimeStr.matches("\\d{1,2}/\\d{1,2}/\\d{4}")) { //// eg. "01/01/2025"
                String[] dateParts = dateTimeStr.split("/");
                String formattedDate = String.format("%s-%02d-%02d",
                        dateParts[2], Integer.parseInt(dateParts[1]), Integer.parseInt(dateParts[0]));
                return LocalDateTime.parse(formattedDate + "T00:00:00");
            } else {
                throw new CheesefoodException("Invalid date format. Use yyyy-mm-dd or dd/mm/yyyy [HHmm]");
            }
        } catch (DateTimeParseException e) {
            throw new CheesefoodException("Invalid date format. Use yyyy-mm-dd or dd/mm/yyyy [HHmm]");
        }
    }

    /**
     * Formats LocalDateTime for display to user
     */
    public static String formatForDisplay(LocalDateTime dateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
        return dateTime.format(formatter);
    }
}