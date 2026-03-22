
/** 
 * Represents a refrigerator product.
 * Includes refrigerator-specific attributes and behaviours.
 */

public class Refrigerator extends Product {
	private String sizeCategory; // e.g., "Small", "Medium", "Large"
	private String refrigeratorType;
	
	public Refrigerator(int barcode, ProductCategory category, String colour, String model, String refrigeratorType, EnergyEfficiency efficiency,
            int quantityInStock, double originalCost, double retailPrice, String sizeCategory) {
		super(barcode, category, colour, model, efficiency, quantityInStock, originalCost, retailPrice);
		this.sizeCategory = sizeCategory;
		this.refrigeratorType = refrigeratorType;
	}
	
	public String getSizeCategory() {
        return sizeCategory;
    }
	
	public String getRefrigeratorType() {
		return refrigeratorType;
	}
	
	@Override
	public String displayWithoutFee() {
	    return ProductDisplayHelper.displayWithoutFee(this);
	}
	
	@Override
	public String toString() {
	    return String.format("Barcode: %d, Category: %s, Type: %s, Model: %s, Colour: %s, Efficiency: %s, Quantity: %d, Retail Price: £%.2f, Size Category: %s",
	            getBarcode(),
	            getProductCategory().name(),
	            getRefrigeratorType(), // e.g., combination, fridge, freezer
	            getModel(),
	            getColour(),
	            getEfficiency().name(),
	            getQuantityInStock(),
	            getRetailPrice(),
	            getSizeCategory());
	}

}