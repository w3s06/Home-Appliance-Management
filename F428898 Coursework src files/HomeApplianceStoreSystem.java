import java.util.Scanner;

/** 
 * Main class that runs the appliance store system.
 * Coordinates user interactions, product management, and reservations.
 */

public class HomeApplianceStoreSystem {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserManager userManager = new UserManager("UserAccounts.txt");
        ReservationManager reservationManager = new ReservationManager("Stock.txt");

        System.out.println("===================================");
        System.out.println("  WELCOME TO THE APPLIANCE STORE  ");
        System.out.println("===================================");

        User loggedInUser = null;
        while (loggedInUser == null) {
            System.out.print("Enter your username: ");
            String username = scanner.nextLine();

            loggedInUser = userManager.authenticate(username);
            if (loggedInUser == null) {
                System.out.println("Invalid username. Please try again.");
            }
        }

        System.out.println("Welcome, " + loggedInUser.getName() + "!");
        interactWithUser(scanner, loggedInUser, reservationManager, new ShoppingBasket());

        scanner.close();
    }

    private static void interactWithUser(Scanner scanner, User user, ReservationManager reservationManager, ShoppingBasket basket) {
        boolean running = true;

        if (user instanceof Admin) {
            printAdminMenu();
        } else {
            printCustomerMenu();
        }

        while (running) {
            System.out.print("\nEnter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            if (user instanceof Admin) {
                switch (choice) {
                    case 1 -> reservationManager.getProductSortedByPrice().forEach(System.out::println);
                    case 2 -> {
                        Product newProduct = createProduct(scanner);
                        ((Admin) user).addProduct(reservationManager, newProduct);
                    }
                    case 3 -> {
                        running = false;
                        System.out.println("Until next time!");
                    }
                    default -> System.out.println("Invalid option.");
                }
            } else {
                switch (choice) {
                    case 1 -> {
                        System.out.println("\n========== PRODUCT CATALOGUE ==========\n");
                        reservationManager.getProductSortedByPrice().forEach(p -> System.out.println(p.displayWithoutFee()));
                    }
                    case 2 -> {
                        System.out.print("Enter barcode: ");
                        int barcode = Integer.parseInt(scanner.nextLine());
                        reservationManager.getProductSortedByPrice().stream()
                                .filter(p -> p.getBarcode() == barcode)
                                .forEach(p -> System.out.println(p.displayWithoutFee()));
                    }
                    case 3 -> {
                        System.out.print("Enter energy rating (A/B/C): ");
                        String rating = scanner.nextLine().toUpperCase();
                        reservationManager.getProductSortedByPrice().stream()
                                .filter(p -> p.getEfficiency().name().equalsIgnoreCase(rating))
                                .forEach(p -> System.out.println(p.displayWithoutFee()));
                    }
                    case 4 -> {
                        System.out.print("Enter barcode to add to basket: ");
                        int barcode = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter quantity: ");
                        int quantity = scanner.nextInt();
                        scanner.nextLine();
                        Product product = reservationManager.getProductSortedByPrice().stream()
                                .filter(p -> p.getBarcode() == barcode)
                                .findFirst()
                                .orElse(null);
                        if (product != null) {
                            basket.addProduct(product, quantity);
                        } else {
                            System.out.println("Product not found.");
                        }
                    }
                    case 5 -> basket.clearBasket();
                    case 6 -> basket.viewBasket();
                    case 7 -> {
                        System.out.print("Choose payment method (in-store/financing): ");
                        String method = scanner.nextLine().trim().toLowerCase();
                        String billingAddress = user.getHouseNumber() + ", " + user.getPostcode() + ", " + user.getCity();

                        if (method.equals("in-store")) {
                            System.out.print("Enter payment type (CreditCard or Cash): ");
                            String paymentTypeInput = scanner.nextLine().trim().toLowerCase();

                            String normalizedPaymentType = switch (paymentTypeInput) {
                                case "creditcard", "credit card" -> "CreditCard";
                                case "cash" -> "Cash";
                                default -> {
                                    System.out.println("Invalid payment type. Please enter 'CreditCard' or 'Cash'.");
                                    yield null;
                                }
                            };

                            if (normalizedPaymentType != null) {
                                basket.reserveInStore(basket.calculateTotal(), normalizedPaymentType, billingAddress, reservationManager);
                            }
                        } else if (method.equals("financing")) {
                            System.out.print("Enter number of monthly instalments: ");
                            int instalments = scanner.nextInt();
                            scanner.nextLine(); // consume newline
                            basket.reserveWithFinancing(basket.calculateTotal(), instalments, billingAddress, reservationManager);
                        } else {
                            System.out.println("Invalid payment method.");
                        }
                    }

                    case 8 -> {
                        running = false;
                        System.out.println("Thank you for visiting! See you next time.");
                    }
                    default -> System.out.println("Invalid option.");
                }
            }
        }
    }

    private static void printCustomerMenu() {
        System.out.println("\n========== CUSTOMER OPTIONS ==========");
        System.out.println("1. View Products");
        System.out.println("2. Search by Barcode");
        System.out.println("3. Filter by Energy Rating");
        System.out.println("4. Add to Basket");
        System.out.println("5. Clear Basket");
        System.out.println("6. View Basket");
        System.out.println("7. Checkout");
        System.out.println("8. Exit");
        System.out.println("======================================");
    }

    private static void printAdminMenu() {
        System.out.println("\n========== ADMIN OPTIONS ==========");
        System.out.println("1. View All Products");
        System.out.println("2. Add Product");
        System.out.println("3. Exit");
        System.out.println("===================================");
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
    

    private static Product createProduct(Scanner scanner) {
        System.out.print("Enter barcode: ");
        int barcode = Integer.parseInt(scanner.nextLine());

        System.out.print("Enter product category (REFRIGERATOR/WASHING_MACHINE): ");
        ProductCategory category = ProductCategory.valueOf(scanner.nextLine().toUpperCase().replace(" ", "_"));

        System.out.print("Enter model name: ");
        String model = scanner.nextLine();

        System.out.print("Enter colour: ");
        String colour = scanner.nextLine();

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
            System.out.print("Enter refrigerator type (e.g., Combination, Fridge, Freezer): ");
            String refrigeratorType = scanner.nextLine();
            return new Refrigerator(barcode, category, colour, model, refrigeratorType, efficiency, quantity, originalCost, retailPrice, sizeCategory);
        } else {
            System.out.print("Enter load type (Front Load/Top Load): ");
            String loadType = scanner.nextLine();
            System.out.print("Enter spin speed (e.g., 1200): ");
            int spinSpeed = Integer.parseInt(scanner.nextLine());
            return new WashingMachine(barcode, category, colour, model, efficiency, quantity, originalCost, retailPrice, loadType, spinSpeed);
        }
    }
}

