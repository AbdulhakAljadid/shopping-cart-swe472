package com.swe472;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// ISP test cases TC1 - TC25 (Base Choice Coverage), see report Section 4.3
public class ShoppingCartIspTest {

    private ShoppingCart cart;

    @BeforeEach
    public void setUp() {
        cart = new ShoppingCart();
    }

    // ---------- addItem (TC1 - TC11) ----------

    // TC1: base case
    @Test
    public void TC01_addItem_base() {
        cart.addItem("Pen", 10.0, 2);
        assertEquals(2, cart.getQuantity("Pen"));
        assertEquals(1, cart.getItemCount());
    }

    // TC2: name is null
    @Test
    public void TC02_addItem_nameNull() {
        try {
            cart.addItem(null, 10.0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue(cart.isEmpty());
    }

    // TC3: name is blank
    @Test
    public void TC03_addItem_nameBlank() {
        try {
            cart.addItem("   ", 10.0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
        assertTrue(cart.isEmpty());
    }

    // TC4: name already in the cart
    @Test
    public void TC04_addItem_existingName() {
        cart.addItem("Pen", 10.0, 2);
        cart.addItem("Pen", 10.0, 2);
        assertEquals(4, cart.getQuantity("Pen"));
        assertEquals(1, cart.getItemCount());
    }

    // TC5: negative price
    @Test
    public void TC05_addItem_priceNegative() {
        try {
            cart.addItem("Pen", -5.0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC6: zero price
    @Test
    public void TC06_addItem_priceZero() {
        try {
            cart.addItem("Pen", 0.0, 2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC7: price with decimals
    @Test
    public void TC07_addItem_priceDecimal() {
        cart.addItem("Pen", 2.5, 2);
        assertEquals(5.0, cart.getSubtotal(), 0.001);
    }

    // TC8: negative quantity
    @Test
    public void TC08_addItem_qtyNegative() {
        try {
            cart.addItem("Pen", 10.0, -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC9: zero quantity
    @Test
    public void TC09_addItem_qtyZero() {
        try {
            cart.addItem("Pen", 10.0, 0);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC10: quantity exactly 1
    @Test
    public void TC10_addItem_qtyOne() {
        cart.addItem("Pen", 10.0, 1);
        assertEquals(1, cart.getQuantity("Pen"));
    }

    // TC11: smallest price 0.01
    @Test
    public void TC11_addItem_priceSmallest() {
        cart.addItem("Pen", 0.01, 2);
        assertEquals(0.02, cart.getSubtotal(), 0.001);
    }

    // ---------- getTotal (TC12 - TC25) ----------

    // TC12: base case, REGULAR member, no coupon
    @Test
    public void TC12_getTotal_base() {
        cart.addItem("Book", 20.0, 1);
        assertEquals(25.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC13: memberType is null
    @Test
    public void TC13_getTotal_memberNull() {
        cart.addItem("Book", 20.0, 1);
        try {
            cart.getTotal(null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC14: SILVER member
    @Test
    public void TC14_getTotal_silver() {
        cart.addItem("Book", 20.0, 1);
        assertEquals(23.0, cart.getTotal("SILVER", null), 0.001);
    }

    // TC15: GOLD member
    @Test
    public void TC15_getTotal_gold() {
        cart.addItem("Book", 20.0, 1);
        assertEquals(21.0, cart.getTotal("GOLD", null), 0.001);
    }

    // TC16: unknown member type
    @Test
    public void TC16_getTotal_memberInvalid() {
        cart.addItem("Book", 20.0, 1);
        try {
            cart.getTotal("VIP", null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    // TC17: coupon is not SAVE10
    @Test
    public void TC17_getTotal_couponOther() {
        cart.addItem("Book", 20.0, 1);
        assertEquals(25.0, cart.getTotal("REGULAR", "ABC"), 0.001);
    }

    // TC18: coupon SAVE10
    @Test
    public void TC18_getTotal_couponSave10() {
        cart.addItem("Book", 20.0, 1);
        assertEquals(15.0, cart.getTotal("REGULAR", "SAVE10"), 0.001);
    }

    // TC19: empty cart
    @Test
    public void TC19_getTotal_emptyCart() {
        assertEquals(0.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC20: small subtotal (0 < subtotal <= 10)
    @Test
    public void TC20_getTotal_smallSubtotal() {
        cart.addItem("Pen", 8.0, 1);
        assertEquals(13.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC21: large subtotal (>= 50)
    @Test
    public void TC21_getTotal_largeSubtotal() {
        cart.addItem("Laptop", 60.0, 1);
        assertEquals(60.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC22: two different items
    @Test
    public void TC22_getTotal_twoItems() {
        cart.addItem("Book", 10.0, 1);
        cart.addItem("Pen", 10.0, 1);
        assertEquals(25.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC23: boundary, subtotal exactly 50.00
    @Test
    public void TC23_getTotal_boundary50() {
        cart.addItem("Item", 50.0, 1);
        assertEquals(50.0, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC24: boundary, subtotal 49.99
    @Test
    public void TC24_getTotal_boundary4999() {
        cart.addItem("Item", 49.99, 1);
        assertEquals(54.99, cart.getTotal("REGULAR", null), 0.001);
    }

    // TC25: coupon bigger than the subtotal
    @Test
    public void TC25_getTotal_couponTooBig() {
        cart.addItem("Pen", 8.0, 1);
        assertEquals(5.0, cart.getTotal("REGULAR", "SAVE10"), 0.001);
    }
}