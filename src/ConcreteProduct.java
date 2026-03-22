
/** 
 * Represents a specific product with concrete attributes.
 * Extends the base Product class to include detailed features.
 */

public class ConcreteProduct extends Product{
	private String additionalInfo;
	
	 public ConcreteProduct(int barcode, ProductCategory category, String colour, String model,
	                           EnergyEfficiency efficiency, int quantityInStock, double originalCost,
	                           double retailPrice, String additionalInfo) {
	        super(barcode, category, colour, model, efficiency, quantityInStock, originalCost, retailPrice);
	        this.additionalInfo = additionalInfo;

	}
	 
	 public String getAdditionalInfo() {
	        return additionalInfo;
	    }
	 
	 @Override
	 public String toString() {
        return String.format(
            "Barcode: %d | Category: %s | Type: %s | Model: %s | Colour: %s | Energy Rating: %s | Quantity: %d | Retail Price: £%.2f | Additional Info: %s",
            getBarcode(), getProductCategory(), getClass().getSimpleName(), getModel(), getColour(),
            getEfficiency(), getQuantityInStock(), getRetailPrice(), additionalInfo
        );
	 }
	 
	 public String displayWithoutFee() {
		 return String.format(
            "Barcode: %d | Category: %s | Model: %s | Colour: %s | Energy Rating: %s | Quantity: %d | Retail Price: £%.2f | Additional Info: %s",
            getBarcode(), getProductCategory(), getModel(), getColour(),
            getEfficiency(), getQuantityInStock(), getRetailPrice(), additionalInfo
        );
	}
}
