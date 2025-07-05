package Services;

import Interfaces.Expirable;
import Interfaces.Shippable;
import Models.Customer;
import Models.Product;
import java.util.List;

public class CheckoutService {
    private final ShippingService shippingService;
    private final PaymentService paymentService;
    private final InventoryService inventoryService;

    public CheckoutService() {
        this.shippingService = new ShippingService();
        this.paymentService = new PaymentService();
        this.inventoryService = new InventoryService();
    }

    public void processCheckout(Customer customer, CartService cart) {
        validateCheckout(customer, cart);

        double subtotal = cart.calculateSubtotal();
        double shippingFee = calculateShippingFee(cart.getShippableItems());
        double totalAmount = subtotal + shippingFee;

        paymentService.processPayment(customer, totalAmount);
        updateProductQuantities(cart);
        shippingService.processShipment(cart.getShippableItems());

        printReceipt(cart, subtotal, shippingFee, totalAmount, customer);
    }

    private void validateCheckout(Customer customer, CartService cart) {
        if (cart.isEmpty()) throw new IllegalStateException("Cannot checkout with empty cart");

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();
            if (product.getQuantity() < item.getQuantity()) {
                throw new IllegalStateException("Product " + product.getName() + " is out of stock");
            }
            if (product instanceof Expirable && ((Expirable) product).isExpired()) {
                throw new IllegalStateException("Product " + product.getName() + " has expired");
            }
        }

        double totalAmount = cart.calculateSubtotal() + calculateShippingFee(cart.getShippableItems());
        if (customer.getBalance() < totalAmount) {
            throw new IllegalStateException("Insufficient balance for checkout");
        }
    }

    private double calculateShippingFee(List<Shippable> shippableItems) {
        return shippableItems.isEmpty() ? 0 : 30 + (shippableItems.size() - 1) * 10;
    }

    private void updateProductQuantities(CartService cart) {
        for (CartItem item : cart.getItems()) {
            inventoryService.updateInventory(item.getProduct(), item.getQuantity());
        }
    }

    private void printReceipt(CartService cart, double subtotal, double shippingFee,
                              double totalAmount, Customer customer) {
        System.out.println("** Checkout receipt **");
        cart.getItems().forEach(item ->
                System.out.printf("%dx %s %.0f%n",
                        item.getQuantity(),
                        item.getProduct().getName(),
                        item.getProduct().getPrice() * item.getQuantity()));
        System.out.println("----------------------");
        System.out.printf("Subtotal %.0f%n", subtotal);
        System.out.printf("Shipping %.0f%n", shippingFee);
        System.out.printf("Amount %.0f%n", totalAmount);
        System.out.printf("Customer balance: %.0f%n", customer.getBalance());
    }
}