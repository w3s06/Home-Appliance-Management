import java.util.*;

/** 
 * Represents a shopping basket for a customer.
 * Stores selected products and calculates totals.
 */

public class ShoppingBasket {
    private Map<Product, Integer> basketItems = new HashMap<>();

    public void addProduct(Product product, int quantity) {
        if (product.getQuantityInStock() >= quantity) {
            basketItems.put(product, basketItems.getOrDefault(product, 0) + quantity);
            System.out.println(quantity + " item(s) added to basket.");
        } else {
            System.out.println("Not enough items in stock!");
        }
    }

    public void viewBasket() {
        if (basketItems.isEmpty()) {
            System.out.println("Your basket is empty.");
        } else {
            System.out.println("Your Basket:");
            for (Map.Entry<Product, Integer> entry : basketItems.entrySet()) {
                System.out.println(entry.getKey().getModel() + " - " + entry.getValue() + " item(s)");
            }
        }
    }

    public double calculateTotal() {
        return basketItems.entrySet().stream()
            .mapToDouble(entry -> entry.getKey().getRetailPrice() * entry.getValue())
            .sum();
    }

    public boolean isEmpty() {
        return basketItems.isEmpty();
    }

    public void reserveInStore(double totalAmount, String paymentType, String billingAddress, ReservationManager reservationManager) {
        ReservationMethod method = new InStorePayment(paymentType, billingAddress);
        ReservationConfirmation confirmation = method.processReservation(totalAmount);
        System.out.println(confirmation.getMessage());

        updateStock(reservationManager);
        clearBasket();
    }

    public void reserveWithFinancing(double totalAmount, int instalments, String billingAddress, ReservationManager reservationManager) {
        ReservationMethod method = new FinancingPayment(instalments, billingAddress);
        ReservationConfirmation confirmation = method.processReservation(totalAmount);
        System.out.println(confirmation.getMessage());

        updateStock(reservationManager);
        clearBasket();
    }

    private void updateStock(ReservationManager reservationManager) {
        for (Map.Entry<Product, Integer> entry : basketItems.entrySet()) {
            reservationManager.updateStockAfterPurchase(entry.getKey().getBarcode(), entry.getValue().intValue(), "Stock.txt");
        }
    }

    public void clearBasket() {
        basketItems.clear();
        System.out.println("Shopping basket cleared.");
    }
}
