package duke;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
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
    public static LocalDateTime parse(String dateTimeString) throws CheesefoodException {
        try {
            // Try parsing with time first
            DateTimeFormatter formatterWithTime = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .append(DateTimeFormatter.ofPattern("yyyy-M-d[ HHmm][ HH:mm]"))
                    .toFormatter();


            DateTimeFormatter formatterWithoutTime = new DateTimeFormatterBuilder()
                    .parseCaseInsensitive()
                    .append(DateTimeFormatter.ofPattern("yyyy-M-d"))
                    .toFormatter();

            try {
                return LocalDateTime.parse(dateTimeString, formatterWithTime);
            } catch (DateTimeParseException e1) {
                LocalDateTime dateOnly = LocalDateTime.parse(dateTimeString + " 0000", formatterWithTime);
                return dateOnly.withHour(0).withMinute(0);
            }

        } catch (DateTimeParseException e) {
            throw new CheesefoodException("Invalid date format: " + dateTimeString +
                    ". Supported formats: 2024-1-1, 2024-01-01, 2024-01-01 2359, 2024/1/1 23:59.");
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




