/** 
 * Provides helper methods for displaying product information.
 * Used to format and present product details to users.
 */

public class ProductDisplayHelper {
    public static String displayWithoutFee(Product event) {
        String display = "Barcode: " + event.getBarcode() + ", Model: " + event.getModel() +
                         ", Colour: " + event.getColour() + ", Category: " + event.getProductCategory() +
                         ", Efficiency: " + event.getEfficiency() + ", Original Cost: " + event.getOriginalCost() + "GBP, Retail Price: " + event.getRetailPrice() + "GBP, "
                         		+ "Stock Available: " + event.getQuantityInStock();
        return display;
    }
}