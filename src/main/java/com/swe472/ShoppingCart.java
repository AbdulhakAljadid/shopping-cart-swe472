package com.swe472;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple shopping cart. Program under test for SWE 472.
 *
 * Member types: REGULAR (no discount), SILVER (10% off), GOLD (20% off).
 * Coupon "SAVE10" takes 10.00 off after the member discount.
 * Orders below 50.00 pay a shipping fee of 5.00.
 */
public class ShoppingCart {

    public static final double FREE_SHIPPING_LIMIT = 50.0;
    public static final double SHIPPING_FEE = 5.0;
    public static final double COUPON_AMOUNT = 10.0;
    public static final double SILVER_RATE = 0.10;
    public static final double GOLD_RATE = 0.20;

    /** One line in the cart. */
    private static class Item {
        private final String name;
        private final double price;
        private int qty;

        Item(String name, double price, int qty) {
            this.name = name;
            this.price = price;
            this.qty = qty;
        }
    }

    private final List<Item> items = new ArrayList<>();

    /**
     * Adds an item. If the name already exists, the quantity is increased.
     *
     * @param name  item name, not null and not empty
     * @param price unit price, must be greater than 0
     * @param qty   quantity, must be greater than 0
     * @throws IllegalArgumentException if any argument is invalid
     */
    public void addItem(String name, double price, int qty) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name must not be empty");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than 0");
        }
        if (qty <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }
        for (Item item : items) {
            if (item.name.equals(name)) {
                item.qty += qty;
                return;
            }
        }
        items.add(new Item(name, price, qty));
    }

    /**
     * Removes an item completely.
     *
     * @return true if the item was found and removed, false otherwise
     */
    public boolean removeItem(String name) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).name.equals(name)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }

    /**
     * Changes the quantity of an existing item. A quantity of 0 removes it.
     *
     * @return true if the item exists, false otherwise
     * @throws IllegalArgumentException if newQty is negative
     */
    public boolean updateQuantity(String name, int newQty) {
        if (newQty < 0) {
            throw new IllegalArgumentException("Quantity must not be negative");
        }
        for (Item item : items) {
            if (item.name.equals(name)) {
                if (newQty == 0) {
                    removeItem(name);
                } else {
                    item.qty = newQty;
                }
                return true;
            }
        }
        return false;
    }

    /** @return quantity of the item, or 0 if it is not in the cart */
    public int getQuantity(String name) {
        for (Item item : items) {
            if (item.name.equals(name)) {
                return item.qty;
            }
        }
        return 0;
    }

    /** @return number of different items in the cart */
    public int getItemCount() {
        return items.size();
    }

    /** @return true if the cart has no items */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /** Removes all items. */
    public void clear() {
        items.clear();
    }

    /**
     * Calculates the final price.
     * Steps: subtotal, member discount, coupon, shipping, rounding.
     *
     * @param memberType "REGULAR", "SILVER" or "GOLD"
     * @param couponCode "SAVE10", any other text, or null (no coupon)
     * @return final price rounded to 2 decimals, or 0 for an empty cart
     * @throws IllegalArgumentException if memberType is invalid
     */
    public double getTotal(String memberType, String couponCode) {
        if (memberType == null) {
            throw new IllegalArgumentException("Member type must not be null");
        }
        if (!memberType.equals("REGULAR")
                && !memberType.equals("SILVER")
                && !memberType.equals("GOLD")) {
            throw new IllegalArgumentException("Unknown member type: " + memberType);
        }

        double subtotal = 0;
        for (Item item : items) {
            subtotal += item.price * item.qty;
        }
        if (subtotal == 0) {
            return 0;
        }

        double rate = 0;
        if (memberType.equals("GOLD")) {
            rate = GOLD_RATE;
        } else if (memberType.equals("SILVER")) {
            rate = SILVER_RATE;
        }
        double total = subtotal * (1 - rate);

        if (couponCode != null && couponCode.equals("SAVE10")) {
            total = total - COUPON_AMOUNT;
            if (total < 0) {
                total = 0;
            }
        }

        if (total < FREE_SHIPPING_LIMIT) {
            total = total + SHIPPING_FEE;
        }

        return Math.round(total * 100.0) / 100.0;
    }
    
    /** @return true if the cart contains an item with this name */
    public boolean hasItem(String name) {
        for (Item item : items) {
            if (item.name.equals(name)) {
                return true;
            }
        }
        return false;
    }

    /** @return sum of price * quantity, before discount, coupon and shipping */
    public double getSubtotal() {
        double sum = 0;
        for (Item item : items) {
            sum += item.price * item.qty;
        }
        return Math.round(sum * 100.0) / 100.0;
    }

    /** @return total number of units in the cart (all quantities added up) */
    public int getTotalUnits() {
        int units = 0;
        for (Item item : items) {
            units += item.qty;
        }
        return units;
    }

    /**
     * Finds the item with the highest line value (price * quantity).
     *
     * @return the item name, or null if the cart is empty
     */
    public String getMostExpensiveItem() {
        if (items.isEmpty()) {
            return null;
        }
        Item best = items.get(0);
        for (int i = 1; i < items.size(); i++) {
            Item current = items.get(i);
            if (current.price * current.qty > best.price * best.qty) {
                best = current;
            }
        }
        return best.name;
    }

    /** @return true if the text is a valid member type (REGULAR, SILVER or GOLD) */
    public static boolean isValidMemberType(String memberType) {
        return "REGULAR".equals(memberType)
                || "SILVER".equals(memberType)
                || "GOLD".equals(memberType);
    }
}