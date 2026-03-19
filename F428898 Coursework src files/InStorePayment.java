
/** 
 * Manages in-store payment transactions.
 * Handles cash or card payments made physically.
 */

public class InStorePayment implements ReservationMethod  {
	private String paymentType; // "Cash" or "Credit/Debit Card" 
	private String billingAddress;
	
	public InStorePayment(String paymentType, String billingAddress) {
		this.paymentType = paymentType;
		setBillingAddress(billingAddress); //ensure validation
	}
	
	public String getBillingAddress() {
	    return billingAddress;
	}
	
	public void setBillingAddress(String billingAddress) {
	    if (billingAddress == null || billingAddress.trim().isEmpty()) {
	        throw new IllegalArgumentException("Billing address cannot be empty.");
	    }

	    // Checks that it contains at least 2 commas (house number, postcode, city)
	    if (billingAddress.split(",").length < 3) {
	        throw new IllegalArgumentException("Billing address must include house number, postcode, and city.");
	    }

	    this.billingAddress = billingAddress;
	}
	
	@Override
	public ReservationConfirmation processReservation(double amount) {
		String today = java.time.LocalDate.now().toString();
		String message = String.format(
				"%.2f to be paid using %s on %s.",
				amount, paymentType, billingAddress,today
				);
		return new ReservationConfirmation(message);

	}
}
