
/** 
 * Abstract base class for all products in the store.
 * Defines common attributes like name, price, and category.
 */

public abstract class Product {
	private int barcode;
	private ProductCategory category;
	private String colour;
	private String model;
	private EnergyEfficiency efficiency;
	private int quantityInStock;
	private double originalCost;
	private double retailPrice;

	// Constructor
	public Product(int barcode, ProductCategory category, String colour, String model, EnergyEfficiency efficiency, 
			int quantityInStock, double originalCost, double retailPrice) {
		this.barcode = barcode;
		this.category = category;
		this.colour = colour;
		this.model = model;
		this.efficiency = efficiency;
		this.quantityInStock = quantityInStock;
		this.originalCost = originalCost;
		this.retailPrice = retailPrice;
	}
	
	public String displayWithoutFee() { // Method to display product details without additional fees
		return "Barcaode: " + barcode + ", Model: " + model + "Colour: " + colour + ", Category: " + category + ", Energy Efficiency: " + efficiency + 
		       ", Original Cost: " + originalCost + " GBP, Retail Price: " + retailPrice + ", Stock Available: " + quantityInStock + "";
	}


	 // Getters
	    public int getBarcode() { return barcode; }
	    public ProductCategory getProductCategory() { return category; }
	    public String getColour() { return colour; }
	    public String getModel() { return model; }
	    public EnergyEfficiency getEfficiency() { return efficiency; }
	    public int getQuantityInStock() { return quantityInStock; }
	    public double getOriginalCost() { return originalCost; }
	    public double getRetailPrice() { return retailPrice; }

    
    public void setQuantityInStock(int newQuantityInStock) { // Updated method to set new stock quantity
            if (newQuantityInStock >= 0) {
				this.quantityInStock = newQuantityInStock;
				// Update the stock	
            } else {
                System.out.println("Sorry, not enough stock available.");
            }
        }
	
    


	public String toFileFormat() {
		return barcode + "," + category + ", " + colour + ", " + model + ", " + efficiency + ", " +
				quantityInStock + ", " + originalCost + ", " + retailPrice;
	}
	
	@Override
	public String toString() {
		return displayWithoutFee();
	}

}
