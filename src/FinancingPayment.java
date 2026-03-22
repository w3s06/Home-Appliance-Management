import java.time.LocalDate;

/** 
 * Handles financing payment options for purchases.
 * Includes logic for installment plans and interest calculations.
 */

public class FinancingPayment implements ReservationMethod {
    private int instalments;
    private String billingAddress;

    public FinancingPayment(int instalments, String billingAddress) {
        this.instalments = instalments;
        this.billingAddress = billingAddress;
    }

    @Override
    public ReservationConfirmation processReservation(double amount) {
        String today = LocalDate.now().toString();
        String message = String.format(
            "%.2f to be paid in %d monthly instalments. The billing address is %s. The first instalment is due %s.",
            amount, instalments, billingAddress, today
        );
        return new ReservationConfirmation(message);
    }
}

 


