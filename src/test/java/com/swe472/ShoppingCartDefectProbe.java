package com.swe472;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

// Explicit diagnostic run only. The standard Surefire test pattern excludes this class.
class ShoppingCartDefectProbe {
    @Test
    void M3_D01_rejectsNaNAsUnitPrice() {
        ShoppingCart cart = new ShoppingCart();
        assertThrows(IllegalArgumentException.class, () -> cart.addItem("Pen", Double.NaN, 1));
    }
}
