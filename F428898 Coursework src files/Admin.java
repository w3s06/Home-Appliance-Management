import java.util.Comparator;
import java.util.List;
import java.util.Scanner;


/** 
 * Represents an administrator in the appliance store system.
 * Handles admin-specific operations and privileges.
 */


class Admin extends User {
    public Admin(String ID, String username, String name, String houseNumber, String postcode, String city, String role) {
        super(ID, username, name, role, houseNumber, postcode, city);
    }

    public void viewAllProducts(List<Product> products) {
        products.sort(Comparator.comparing(Product::getRetailPrice));
        for (Product product : products) {
            System.out.println(product.displayWithoutFee());
        }
    }

    public void addProduct(ReservationManager reservationManager, Product newProduct) {
        if (!reservationManager.productExists(newProduct.getBarcode())) {
            reservationManager.addProduct(newProduct);
            System.out.println("Product has been added successfully.");
        } else {
            System.out.println("ERROR: Product already exists, cannot add duplicate.");
        }
    }
    
    private static EnergyEfficiency promptForEnergyEfficiency(Scanner scanner) {
        while (true) {
            System.out.print("Enter energy rating (A/B/C): ");
            String input = scanner.nextLine().trim().toUpperCase();

            try {
                return EnergyEfficiency.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid energy rating. Please choose between A, B, or C.");
            }
        }
    }


    public static void interactWithUser(Scanner scanner, User user, ReservationManager reservationManager) {
        if (user instanceof Admin) {
            System.out.println("Admin options: \n1. View all products \n2. Add product.");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
            	((Admin) user).viewAllProducts(reservationManager.getProductSortedByPrice());
            } else if (choice == 2) {
                Product newProduct = createProduct(scanner);
                ((Admin) user).addProduct(reservationManager, newProduct);
            }
        } else {
            System.out.println("Customer options: \n1. View Products \n2. Search by Barcode \n3. Filter by Energy Rating");
            int choice = scanner.nextInt();
            scanner.nextLine();

            if (choice == 1) {
                reservationManager.getProductSortedByPrice().forEach(p -> System.out.println(p.displayWithoutFee()));
            } else if (choice == 2) {
                System.out.print("Enter barcode: ");
                int barcode = Integer.parseInt(scanner.nextLine());
                reservationManager.getProductSortedByPrice().stream()
                        .filter(p -> p.getBarcode() == barcode)
                        .forEach(p -> System.out.println(p.displayWithoutFee()));
            } else if (choice == 3) {
                System.out.print("Enter energy rating (A/B/C): ");
                String rating = scanner.nextLine();
                reservationManager.getProductSortedByPrice().stream()
                        .filter(p -> p.getEfficiency().name().equalsIgnoreCase(rating))
                        .forEach(p -> System.out.println(p.displayWithoutFee()));
            }
        }
    }

    private static Product createProduct(Scanner scanner) {
        System.out.print("Enter barcode: ");
        int barcode = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter model name: ");
        String model = scanner.nextLine();
        System.out.print("Enter colour: ");
        String colour = scanner.nextLine();
        System.out.print("Enter category (Refrigeration/Washing): ");
        ProductCategory category = ProductCategory.valueOf(scanner.nextLine().toUpperCase());
        EnergyEfficiency efficiency = promptForEnergyEfficiency(scanner);
        System.out.print("Enter quantity in stock: ");
        int quantity = Integer.parseInt(scanner.nextLine());
        System.out.print("Enter original cost: ");
        double originalCost = Double.parseDouble(scanner.nextLine());
        System.out.print("Enter retail price: ");
        double retailPrice = Double.parseDouble(scanner.nextLine());

        if (category == ProductCategory.REFRIGERATOR) {
            System.out.print("Enter size category (Small/Medium/Large): ");
            String sizeCategory = scanner.nextLine();
            System.out.print("Enter refrigerator type (e.g., combination, fridge, freezer): ");
            String refrigeratorType = scanner.nextLine();
            return new Refrigerator(barcode, category, colour, model, refrigeratorType, efficiency, quantity, originalCost, retailPrice, sizeCategory);
        } else {
            System.out.print("Enter load type (top-load/front-load): ");
            String loadType = scanner.nextLine();
            System.out.print("Enter spin speed (e.g., 1200): ");
            int spinSpeed = Integer.parseInt(scanner.nextLine());
            return new WashingMachine(barcode, category, colour, model, efficiency, quantity, originalCost, retailPrice, loadType, spinSpeed);
        }
    }
}
