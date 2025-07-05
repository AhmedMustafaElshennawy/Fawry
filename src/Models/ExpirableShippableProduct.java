package Models;

import Interfaces.Expirable;
import Interfaces.Shippable;

import java.util.Date;

public class ExpirableShippableProduct extends Product implements Shippable, Expirable {
    private double weight;
    private Date expiryDate;

    public ExpirableShippableProduct(String name, double price, int quantity, double weight, Date expiryDate) {
        super(name, price, quantity);
        this.weight = weight;
        this.expiryDate = expiryDate;
    }

    @Override public boolean requiresShipping() { return true; }
    @Override public double getWeight() { return weight; }
    @Override public boolean isExpired() { return new Date().after(expiryDate); }
}