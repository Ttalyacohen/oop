package tests;

import org.junit.jupiter.api.Test;
import realestate.property.Address;

import static org.junit.jupiter.api.Assertions.*;

class AddressTest {

    @Test
    void testBasicAddressCreation() {
        Address address = new Address(new int[]{1, 2});
        assertEquals("(1,2)", address.toString());
    }

    @Test
    void testStringConstructor() {
        Address address = new Address("(1,2)");
        assertEquals("(1,2)", address.toString());
    }

    @Test
    void testIsWithinRadius() {
        Address center = new Address(new int[]{5, 5});
        Address test = new Address(new int[]{4, 4});

        assertTrue(test.isWithinRadius(center, 2));
        assertFalse(test.isWithinRadius(center, 1));
    }
    @Test
    void testNestedAddress() {
        // בדיקת כתובת מקוננת (דירה בתוך דירה)
        Address nestedAddress = new Address(new int[]{1, 2, 3, 4, 5});
        assertEquals("(1,2,3,4,5)", nestedAddress.toString());
    }

    @Test
    void testEdgeCaseStrings() {
        // בודק מקרה של כתובת עם אפסים
        Address address1 = new Address("(0,0)");
        assertEquals("(0,0)", address1.toString());

        // בודק מקרה של מספרים גדולים
        Address address2 = new Address("(999,999)");
        assertEquals("(999,999)", address2.toString());

        // בודק מקרה של כתובת ארוכה
        Address address3 = new Address("(1,2,3,4,5)");
        assertEquals("(1,2,3,4,5)", address3.toString());
    }

    @Test
    void testToStringEdgeCases() {
        // בדיקת מקרי קצה של המרה למחרוזת
        Address zeroAddress = new Address(new int[]{0, 0});
        assertEquals("(0,0)", zeroAddress.toString());

        Address longAddress = new Address(new int[]{1, 2, 3, 4, 5});
        assertEquals("(1,2,3,4,5)", longAddress.toString());

        Address singleDigitAddress = new Address(new int[]{1, 1});
        assertEquals("(1,1)", singleDigitAddress.toString());
    }

    @Test
    void testEquals() {
        Address address1 = new Address(new int[]{1, 2});
        Address address2 = new Address(new int[]{1, 2});
        Address address3 = new Address(new int[]{2, 1});

        assertTrue(address1.equals(address2));
        assertFalse(address1.equals(address3));
        assertFalse(address1.equals(null));
    }
}