
import Models.*;
import Services.CartService;
import Services.CheckoutService;

import java.util.Date;

public class Main {
    public static void main(String[] args) {
        runSuccessCases();
        runFailureCases();
    }

    private static void runSuccessCases() {
        System.out.println("=== RUNNING SUCCESS CASES ===");

        Product tv = new NonExpirableShippableProduct("TV", 1000, 3, 15.0);
        CartService cart = new CartService();
        cart.addItem(tv, 1);

        Customer customer = new Customer("John Doe", 2000);
        CheckoutService checkoutService = new CheckoutService();

        checkoutService.processCheckout(customer, cart);
        System.out.println("Checkout successful!");
    }

    private static void runFailureCases() {
        System.out.println("\n=== RUNNING FAILURE CASES ===");

        CheckoutService checkoutService = new CheckoutService();

        try {
            checkoutService.processCheckout(new Customer("Test", 100), new CartService());
        } catch (IllegalStateException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

        try {
            Product expired = new ExpirableShippableProduct("Milk", 2, 5, 0.5,
                    new Date(System.currentTimeMillis() - 1000));
            CartService cart = new CartService();
            cart.addItem(expired, 1);
            checkoutService.processCheckout(new Customer("Test", 100), cart);
        } catch (IllegalStateException e) {
            System.out.println("Caught expected error: " + e.getMessage());
        }

    }
}