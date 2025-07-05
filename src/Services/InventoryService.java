package Services;

import Models.Product;

class InventoryService {
    public void updateInventory(Product product, int quantitySold) {
        product.setQuantity(product.getQuantity() - quantitySold);
    }
}
