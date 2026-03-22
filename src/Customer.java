import java.util.Comparator;
import java.util.List;

/** 
 * Represents a customer using the appliance store system.
 * Stores customer details and interactions.
 */


class Customer extends User {
    private ShoppingBasket basket;

    public Customer(String ID, String username, String name, String houseNumber, String postcode, String city, String role) {
        super(ID, username, name, houseNumber, postcode, city, role);
        this.basket = new ShoppingBasket();
    }

    public void viewProducts(List<Product> products) {
        products.sort(Comparator.comparing(Product::getRetailPrice));
        for (Product product : products) {
            System.out.println(product.displayWithoutFee());
        }
    }

    public void addToBasket(Product product, int quantity) {
        basket.addProduct(product, quantity);
        System.out.println(quantity + " item(s) added to basket.");
    }

    public void viewBasket() {
        basket.viewBasket();
    }

    public void reserve(String method, String paymentDetails, int instalments, ReservationManager reservationManager) {
        if (basket.isEmpty()) {
            System.out.println("Your basket is empty. Add products before reserving.");
            return;
        }

        System.out.println("Reservation:");
        double totalAmount = basket.calculateTotal();
        String billingAddress = getHouseNumber() + ", " + getPostcode() + ", " + getCity();

        if (method.equalsIgnoreCase("financing")) {
            basket.reserveWithFinancing(totalAmount, instalments, billingAddress, reservationManager);
        } else {
            basket.reserveInStore(totalAmount, method, billingAddress, reservationManager);
        }
    }

    public void cancelBasket() {
        basket.clearBasket();
        System.out.println("Shopping basket cleared.");
    }
}

