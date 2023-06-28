import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class Product {
    private String name;
    private double price;
    private int quantityInStock;
    private int quantityInCart;

    public Product(String name, double price, int quantityInStock) {
        this.name = name;
        this.price = price;
        this.quantityInStock = quantityInStock;
        this.quantityInCart = 0;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantityInStock() {
        return quantityInStock;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public void updateQuantityInStock(int quantity) {
        quantityInStock -= quantity;
    }
}

class Customer {
    private String firstName;
    private String lastName;
    private int verificationCode;
    private boolean isWholesaleCustomer;
    private boolean isRegularCustomer;
    private double walletAmount;

    public Customer(String firstName, String lastName, int verificationCode, boolean isWholesaleCustomer, boolean isRegularCustomer, double walletAmount) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.verificationCode = verificationCode;
        this.isWholesaleCustomer = isWholesaleCustomer;
        this.isRegularCustomer = isRegularCustomer;
        this.walletAmount = walletAmount;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getVerificationCode() {
        return verificationCode;
    }

    public boolean isWholesaleCustomer() {
        return isWholesaleCustomer;
    }

    public boolean isRegularCustomer() {
        return isRegularCustomer;
    }

    public double getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(double walletAmount) {
        this.walletAmount = walletAmount;
    }
}

public class Main {

    public static void main(String[] args) {
        ArrayList<Product> products = loadProductsFromFile("magazyn.txt");
        Scanner scanner = new Scanner(System.in);

        System.out.print("Jesteś klientem detalicznym? (Tak/Nie): ");
        String retailChoice = scanner.nextLine();
        boolean isRetailCustomer = retailChoice.equalsIgnoreCase("Tak");

        if (isRetailCustomer) {
            System.out.print("Podaj ilość pieniędzy w portfelu: ");
            double walletAmount = scanner.nextDouble();
            scanner.nextLine();
            Customer customer = new Customer("", "", 0, false, false, walletAmount);
            processOrder(customer, products);

        } else {
            System.out.print("Czy jesteś stałym klientem? (Tak/Nie): ");
            String regularChoice = scanner.nextLine();

            System.out.print("Czy jesteś klientem hurtowym? (Tak/Nie): ");
            String wholesaleChoice = scanner.nextLine();

            boolean isRegularCustomer = regularChoice.equalsIgnoreCase("Tak");
            boolean isWholesaleCustomer = wholesaleChoice.equalsIgnoreCase("Tak");

            if (isRegularCustomer && isWholesaleCustomer) {
                System.out.print("Podaj imię: ");
                String firstName = scanner.nextLine();
                System.out.print("Podaj nazwisko: ");
                String lastName = scanner.nextLine();
                System.out.print("Podaj 5-cyfrowy kod: ");
                int verificationCode = scanner.nextInt();
                scanner.nextLine();

                boolean isRegistered = checkCustomerRegistration(firstName, lastName, verificationCode);
                if (isRegistered) {
                    System.out.print("Podaj ilość pieniędzy w portfelu: ");
                    double walletAmount = scanner.nextDouble();
                    scanner.nextLine();

                    Customer customer = new Customer(firstName, lastName, verificationCode, true, true, walletAmount);
                    System.out.println("Zalogowano jako stały klient hurtowy.");
                    processOrder(customer, products);
                } else {
                    System.out.println("Niepoprawne dane logowania.");
                }
            } else if (isRegularCustomer) {
                System.out.print("Podaj imię: ");
                String firstName = scanner.nextLine();
                System.out.print("Podaj nazwisko: ");
                String lastName = scanner.nextLine();
                System.out.print("Podaj 5-cyfrowy kod: ");
                int verificationCode = scanner.nextInt();
                scanner.nextLine();

                boolean isRegistered = checkCustomerRegistration(firstName, lastName, verificationCode);
                if (isRegistered) {
                    System.out.print("Podaj ilość pieniędzy w portfelu: ");
                    double walletAmount = scanner.nextDouble();
                    scanner.nextLine();

                    Customer customer = new Customer(firstName, lastName, verificationCode, false, true, walletAmount);
                    System.out.println("Zalogowano jako stały klient detaliczny.");
                    processOrder(customer, products);
                } else {
                    System.out.println("Niepoprawne dane logowania.");
                }
            } else if (isWholesaleCustomer) {
                System.out.print("Podaj ilość pieniędzy w portfelu: ");
                double walletAmount = scanner.nextDouble();
                scanner.nextLine();

                Customer customer = new Customer("", "", 0, true, false, walletAmount);
                System.out.println("Zalogowano jako klient hurtowy.");
                processOrder(customer, products);
            } else {
                System.out.println("Niepoprawne dane logowania.");
            }
        }
    }

    private static ArrayList<Product> loadProductsFromFile(String filename) {
        ArrayList<Product> products = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4) {
                    int productId = Integer.parseInt(data[0]);
                    String productName = data[1];
                    double price = Double.parseDouble(data[2]);
                    int quantityInStock = Integer.parseInt(data[3]);
                    Product product = new Product(productName, price, quantityInStock);
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu pliku.");
        }
        return products;
    }

    private static boolean checkCustomerRegistration(String firstName, String lastName, int verificationCode) {
        try (BufferedReader reader = new BufferedReader(new FileReader("baza.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    String storedFirstName = data[0];
                    String storedLastName = data[1];
                    int storedVerificationCode = Integer.parseInt(data[2]);
                    if (storedFirstName.equalsIgnoreCase(firstName)
                            && storedLastName.equalsIgnoreCase(lastName)
                            && storedVerificationCode == verificationCode) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Błąd odczytu pliku baza.txt.");
        }
        return false;
    }

    private static void processOrder(Customer customer, ArrayList<Product> products) {
        Scanner scanner = new Scanner(System.in);

        String choice;

        do {
            System.out.println("\nLista dostępnych produktów:");
            for (int i = 0; i < products.size(); i++) {
                Product product = products.get(i);
                System.out.println((i + 1) + ". " + product.getName() + " - Cena: " + product.getPrice() + " zł - Ilość dostępna: " + product.getQuantityInStock());
            }

            System.out.print("\nPodaj numer produktu, który chcesz dodać do koszyka (lub '0' aby zakończyć): ");
            choice = scanner.nextLine();

            if (!choice.equals("0")) {
                int productIndex = Integer.parseInt(choice) - 1;

                if (productIndex >= 0 && productIndex < products.size()) {
                    Product selectedProduct = products.get(productIndex);

                    System.out.print("Podaj ilość sztuk: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine();

                    if (selectedProduct.getQuantityInStock() >= quantity) {
                        selectedProduct.updateQuantityInStock(quantity);
                        selectedProduct.setQuantityInCart(quantity);
                        System.out.println("Produkt dodany do koszyka.");

                        // Wyświetlanie wartości bieżącego koszyka
                        double currentCartValue = 0;
                        for (Product product : products) {
                            currentCartValue += product.getPrice() * product.getQuantityInCart();
                        }
                        System.out.println("Wartość bieżącego koszyka: " + currentCartValue + " zł");
                    } else {
                        System.out.println("Niewystarczająca ilość produktu.");
                    }
                } else {
                    System.out.println("Nieprawidłowy numer produktu.");
                }
            }
        } while (!choice.equals("0"));

        double totalPrice = 0;

        System.out.println("\nZawartość koszyka:");
        for (Product product : products) {
            if (product.getQuantityInCart() > 0) {
                System.out.println("- " + product.getName() + " - Cena: " + product.getPrice() + " zł - Ilość: " + product.getQuantityInCart());
                totalPrice += product.getPrice() * product.getQuantityInCart();
            }
        }

        System.out.println("\nŁączna cena zamówienia: " + totalPrice + " zł");

        if (customer.isWholesaleCustomer() && customer.isRegularCustomer()) {
            double finalPrice = totalPrice;
            System.out.println("Cena do zapłaty: " + finalPrice + " zł");

            // Aktualizacja portfela klienta
            double remainingAmount = customer.getWalletAmount() - finalPrice;
            if (remainingAmount >= 0) {
                customer.setWalletAmount(remainingAmount);
                System.out.println("Zamówienie zostało przyjęte.");
                System.out.println("Pozostała kwota w portfelu: " + remainingAmount + " zł");
            } else {
                System.out.println("Brak wystarczających środków w portfelu.");
            }
        } else if (customer.isWholesaleCustomer()) {
            double finalPrice = totalPrice;
            System.out.println("Cena do zapłaty: " + finalPrice + " zł");

            // Aktualizacja portfela klienta
            double remainingAmount = customer.getWalletAmount() - finalPrice;
            if (remainingAmount >= 0) {
                customer.setWalletAmount(remainingAmount);
                System.out.println("Zamówienie zostało przyjęte.");
                System.out.println("Pozostała kwota w portfelu: " + remainingAmount + " zł");
            } else {
                System.out.println("Brak wystarczających środków w portfelu.");
            }
        } else if (customer.isRegularCustomer()) {
            double finalPrice = totalPrice;
            System.out.println("Cena do zapłaty  " + finalPrice + " zł");

            // Aktualizacja portfela klienta
            double remainingAmount = customer.getWalletAmount() - finalPrice;
            if (remainingAmount >= 0) {
                customer.setWalletAmount(remainingAmount);
                System.out.println("Zamówienie zostało przyjęte.");
                System.out.println("Pozostała kwota w portfelu: " + remainingAmount + " zł");
            } else {
                System.out.println("Brak wystarczających środków w portfelu.");
            }
        } else {
            double finalPrice = totalPrice;
            System.out.println("Cena do zapłaty: " + finalPrice + " zł");

            // Aktualizacja portfela klienta
            double remainingAmount = customer.getWalletAmount() - finalPrice;
            if (remainingAmount >= 0) {
                customer.setWalletAmount(remainingAmount);
                System.out.println("Zamówienie zostało przyjęte.");
                System.out.println("Pozostała kwota w portfelu: " + remainingAmount + " zł");
            } else {
                System.out.println("Brak wystarczających środków w portfelu.");
            }
        }
    }
}

