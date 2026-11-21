package com.swe472;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class ShoppingCartAdditionalTest {
    private final ShoppingCart cart = new ShoppingCart();

    @Test
    void M3_U01_updatesExistingItem() {
        cart.addItem("Pen", 2.50, 2);
        assertTrue(cart.updateQuantity("Pen", 4));
        assertEquals(4, cart.getQuantity("Pen"));
        assertEquals(10.00, cart.getSubtotal(), 0.001);
    }

    @Test
    void M3_U02_zeroRemovesExistingItem() {
        cart.addItem("Pen", 2.50, 2);
        cart.addItem("Book", 10.00, 1);
        assertTrue(cart.updateQuantity("Pen", 0));
        assertFalse(cart.hasItem("Pen"));
        assertEquals(1, cart.getItemCount());
        assertEquals(1, cart.getQuantity("Book"));
    }

    @Test
    void M3_U03_missingNameLeavesCartUnchanged() {
        cart.addItem("Pen", 2.50, 2);
        assertFalse(cart.updateQuantity("Book", 3));
        assertEquals(1, cart.getItemCount());
        assertEquals(2, cart.getQuantity("Pen"));
    }

    @Test
    void M3_U04_rejectsNegativeQuantityWithoutMutation() {
        cart.addItem("Pen", 2.50, 2);
        assertThrows(IllegalArgumentException.class, () -> cart.updateQuantity("Pen", -1));
        assertEquals(2, cart.getQuantity("Pen"));
    }

    @Test
    void M3_G01_validatesMembershipEvenWhenCartIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> cart.getTotal("VIP", null));
    }

    @Test
    void M3_G02_usesPostDiscountAndCouponAmountForShipping() {
        cart.addItem("Book", 70.00, 1);
        assertEquals(51.00, cart.getTotal("GOLD", "SAVE10"), 0.001);
    }

    @Test
    void M3_G03_couponLeavesExactlyFreeShippingThreshold() {
        cart.addItem("Book", 60.00, 1);
        assertEquals(50.00, cart.getTotal("REGULAR", "SAVE10"), 0.001);
    }

    @Test
    void M3_G04_roundsFractionalCentsAfterDiscount() {
        cart.addItem("Book", 12.34, 1);
        assertEquals(16.11, cart.getTotal("SILVER", null), 0.001);
    }
}
