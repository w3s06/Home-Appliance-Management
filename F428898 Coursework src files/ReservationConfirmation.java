
/** 
 * Generates and manages reservation confirmations.
 * Ensures users receive proper documentation of their bookings.
 */

public class ReservationConfirmation {
    private String message;

    public ReservationConfirmation(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return message;
    }
}
