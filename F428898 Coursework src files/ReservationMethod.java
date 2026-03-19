/** 
 * Defines different methods of making reservations.
 * Could include online, in-store, or phone-based options.
 */

public interface ReservationMethod {
	ReservationConfirmation processReservation(double amount);

}
