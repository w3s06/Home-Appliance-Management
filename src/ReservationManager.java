import java.io.*;
import java.util.*;
import java.util.logging.*;

/** 
 * Manages the reservation system for products.
 * Handles booking, availability checks, and cancellations.
 */

public class ReservationManager {
    private List<Product> products = Collections.synchronizedList(new ArrayList<>());
    private static final Logger logger = Logger.getLogger(ReservationManager.class.getName());

    public ReservationManager(String filePath) {
        loadProducts(filePath);
    }

    private void loadProducts(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                try {
                    Product product = createProduct(parts);
                    if (product != null) {
                        products.add(product);
                    }
                } catch (IllegalArgumentException e) {
                    logger.warning("Invalid product data: " + Arrays.toString(parts) + " - " + e.getMessage());
                }
            }
        } catch (IOException e) {
            logger.severe("Error reading file: " + e.getMessage());
        }
    }

    private Product createProduct(String[] data) {
        if (data.length != 10) {
            throw new IllegalArgumentException("Invalid product data format: " + Arrays.toString(data));
        }

        int barcode = Integer.parseInt(data[0].trim());
        
        //Normalise and validate ProductCategory
        String rawCategory = data[1].trim().toUpperCase().replace(" ", "_");
        ProductCategory category;
        try {
        	category = ProductCategory.valueOf(rawCategory);
        } catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Invalid product category: " + data[1]);
		}
        String type = data[2].trim();
        String model = data[3].trim();
        String colour = data[4].trim();
        
        EnergyEfficiency efficiency = validateEnum(EnergyEfficiency.class, data[5]);
        
        int quantityInStock = Integer.parseInt(data[6].trim());
        double originalCost = Double.parseDouble(data[7].trim());
        double retailPrice = Double.parseDouble(data[8].trim());
        String extra = data[9].trim();

        if (category == ProductCategory.REFRIGERATOR) {
        	String refrigeratorType = type; // from data[2]
        	String sizeCategory = extra;
        	return new Refrigerator(barcode, category, colour, model, refrigeratorType, efficiency, quantityInStock, originalCost, retailPrice, sizeCategory);
        } else {
            int spinSpeed;
            try {
                spinSpeed = Integer.parseInt(extra);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid spin speed: " + extra);
            }
            return new WashingMachine(barcode, category, colour, model, efficiency, quantityInStock, originalCost, retailPrice, type, spinSpeed);
        }
    }

    private <T extends Enum<T>> T validateEnum(Class<T> enumClass, String value) {
        try {
            return Enum.valueOf(enumClass, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid enum value for " + enumClass.getSimpleName() + ": " + value);
        }
    }

    public boolean productExists(int barcode) {
        synchronized (products) {
            return products.stream().anyMatch(p -> p.getBarcode() == barcode);
        }
    }

    public void addProduct(Product newProduct) {
        synchronized (products) {
            if (newProduct != null) {
                products.add(newProduct);
                saveAllProductsToFile("Stock.txt");
                logger.info("Product added and saved successfully.");
            } else {
                logger.warning("Attempted to add a null product.");
            }
        }
    }

    public void updateStockAfterPurchase(int barcode, int quantitySold, String filePath) {
        synchronized (products) {
            for (Product product : products) {
                if (product.getBarcode() == barcode) {
                    int newQuantity = product.getQuantityInStock() - quantitySold;
                    if (newQuantity < 0) {
                        logger.warning("Insufficient stock for product barcode: " + barcode);
                        return;
                    }

                    product.setQuantityInStock(newQuantity);

                    // Update the specific line in Stock.txt
                    updateStockFileLine(barcode, newQuantity, filePath);
                    return;
                }
            }
            logger.warning("Product barcode not found: " + barcode);
        }
    }

    private void updateStockFileLine(int barcode, int newQuantity, String filePath) {
        File originalFile = new File(filePath);
        File tempFile = new File(filePath + ".tmp");

        try (BufferedReader reader = new BufferedReader(new FileReader(originalFile));
             BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",\\s*");
                if (parts.length >= 7 && Integer.parseInt(parts[0].trim()) == barcode) {
                    parts[6] = String.valueOf(newQuantity); // update quantity
                    line = String.join(",", parts);
                }
                writer.write(line);
                writer.newLine();
            }

        } catch (IOException e) {
            logger.severe("Error updating stock file: " + e.getMessage());
            return;
        }

        // Replace original file with updated temp file
        if (!originalFile.delete() || !tempFile.renameTo(originalFile)) {
            logger.severe("Failed to replace original stock file with updated file.");
        }
    }

    private void saveAllProductsToFile(String filePath) {
        synchronized (products) {
            if (products.isEmpty()) {
                logger.warning("No products to save. File not updated.");
                return;
            }
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
                for (Product product : products) {
                    writer.write(formatProductLine(product));
                    writer.newLine();
                }
            } catch (IOException e) {
                logger.severe("Error writing products to file: " + e.getMessage());
            }
        }
    }

    private String formatProductLine(Product product) {
    	if (product instanceof Refrigerator r) {
    		return String.format("%d,%s,%s,%s,%s,%s,%d,%.2f,%.2f,%s",
    			r.getBarcode(),
    			r.getProductCategory().name(),
    			r.getRefrigeratorType(),
                r.getModel(),
                r.getColour(),
                r.getEfficiency().name(),
                r.getQuantityInStock(),       // int → %d
                r.getOriginalCost(),          // double → %.2f
                r.getRetailPrice(),           // double → %.2f
                r.getSizeCategory());         // String → %s
    	} else if (product instanceof WashingMachine wm) {
    		return String.format("%d,%s,%s,%s,%s,%s,%d,%.2f,%.2f,%d",
                wm.getBarcode(),
                wm.getProductCategory().name(),
                wm.getLoadType(),
                wm.getModel(),
                wm.getColour(),
                wm.getEfficiency().name(),
                wm.getQuantityInStock(),      // int → %d
                wm.getOriginalCost(),         // double → %.2f
                wm.getRetailPrice(),          // double → %.2f
                wm.getSpinSpeed());           // int → %d
    	} else {
    		throw new IllegalArgumentException("Unknown product type");
    	}
    }






    public List<Product> getProductSortedByPrice() {
        synchronized (products) {
            products.sort(Comparator.comparing(Product::getRetailPrice));
            return new ArrayList<>(products);
        }
    }

    public List<Product> searchByEnergyRating(String rating) {
    	EnergyEfficiency efficiency;
    	try {
    		efficiency = EnergyEfficiency.valueOf(rating.toUpperCase());
    	} catch (IllegalArgumentException e) {
    		logger.warning("Invalid energy rating: " + rating);
    		return Collections.emptyList();
    	}

    	synchronized (products) {
    		List<Product> result = new ArrayList<>();
    		for (Product product : products) {
    			if (product.getEfficiency() == efficiency) {
    				result.add(product);
    			}
    		}
    		return result;
    	}
    }
}


