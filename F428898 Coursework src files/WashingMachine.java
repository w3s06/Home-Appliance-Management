
/**
 * Represents a washing machine product in the appliance store.
 * Includes washing machine-specific attributes such as load capacity, spin speed, and energy rating.
 * Extends the Product class to provide specialised functionality.
 */

public class WashingMachine extends Product {
    private String loadType; // e.g., "Front Load", "Top Load"
    private int spinSpeed; // e.g., 1200, 1400, 1600 RPM

    public WashingMachine(int barcode, ProductCategory category, String colour, String model, EnergyEfficiency efficiency,
                            int quantityInStock, double originalCost, double retailPrice, String loadType, int spinSpeed) { // Constructor
        super(barcode, category, colour, model, efficiency, quantityInStock, originalCost, retailPrice);
        this.loadType = loadType;
        this.spinSpeed = spinSpeed;
    }

    public String getLoadType() { // e.g., "Front Load", "Top Load"
        return loadType;
    }
    

    public int getSpinSpeed() { // e.g., 1200, 1400, 1600 RPM
        return spinSpeed;
    }


    @Override
    public String displayWithoutFee() { 
        return ProductDisplayHelper.displayWithoutFee(this);
    }
    
    @Override
    public String toString() {
        return String.format("Barcode: %d, Category: %s, Type: %s, Model: %s, Colour: %s, Efficiency: %s, Quantity: %d, Retail Price: £%.2f, Spin Speed: %d RPM",
                getBarcode(),
                getProductCategory().name(),
                getLoadType(), // top-load or front-load
                getModel(),
                getColour(),
                getEfficiency().name(),
                getQuantityInStock(),
                getRetailPrice(),
                getSpinSpeed());
    }

}
