package Services;

import Interfaces.Shippable;

import java.util.List;

class ShippingService {
    public void processShipment(List<Shippable> items) {
        if (items.isEmpty()) return;

        System.out.println("** Shipment notice **");
        items.stream()
                .collect(java.util.stream.Collectors.groupingBy(
                        item -> item.getName() + " " + (int)(item.getWeight() * 1000) + "g",
                        java.util.stream.Collectors.counting()))
                .forEach((desc, count) -> System.out.println(count + "x " + desc));

        double totalWeight = items.stream().mapToDouble(Shippable::getWeight).sum();
        System.out.printf("Total package weight %.1fkg%n", totalWeight);
    }
}
