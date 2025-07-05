package Services;

import Interfaces.Shippable;
import Models.Product;
import java.util.ArrayList;
import java.util.List;

public class CartService {
    private List<CartItem> items = new ArrayList<>();

    public void addItem(Product product, int quantity) {
        validateAddToCart(product, quantity);
        items.add(new CartItem(product, quantity));
    }

    private void validateAddToCart(Product product, int quantity) {
        if (quantity <= 0) throw new IllegalArgumentException("Quantity must be positive");
        if (product.getQuantity() < quantity) {
            throw new IllegalArgumentException("Not enough stock for " + product.getName());
        }
    }

    public double calculateSubtotal() {
        return items.stream()
                .mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }

    public List<CartItem> getItems() { return new ArrayList<>(items); }
    public boolean isEmpty() { return items.isEmpty(); }

    public List<Shippable> getShippableItems() {
        List<Shippable> shippableItems = new ArrayList<>();
        for (CartItem item : items) {
            Product product = item.getProduct();
            if (product instanceof Shippable) {
                for (int i = 0; i < item.getQuantity(); i++) {
                    shippableItems.add((Shippable) product);
                }
            }
        }
        return shippableItems;
    }
}
