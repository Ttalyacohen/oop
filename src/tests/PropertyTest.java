package tests;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import realestate.property.Property;
import realestate.property.Address;
import realestate.property.PropertyManager;
import realestate.user.Realtor;
import realestate.user.Seller;
import exceptions.UnauthorizedAccessException;

public class PropertyTest {
    private Property property;
    private Address address;
    private Seller seller;
    private Realtor realtor;
    private static final double SQUARE_METERS = 100.0;
    private static final int PRICE = 1000000;

    @Before
    public void setUp() {
        property = new Property(
                new Property.PropertyInfo(new Address(new int[]{1, 2}), SQUARE_METERS),
                PRICE, false,
                new Seller("S123"), new Realtor("R456")
        );
    }

    // 1. בדיקות constructor והגטרים
    @Test
    public void testPropertyCreation() {
        assertNotNull("Property should be created", property);
        assertEquals("Address should match", address, property.getInfo().address());
        assertEquals("Square meters should match", SQUARE_METERS, property.getInfo().squareMeters(), 0.001);
        assertEquals("Price should match", PRICE, property.getPrice());
        assertFalse("New property should not be sold", property.getIsSold());
        assertEquals("Seller should match", seller, property.getSeller());
        assertEquals("Realtor should match", realtor, property.getRealtor());
    }

    // 2. בדיקות setIsSold
    @Test
    public void testSetIsSoldWithAuthorizedRealtor() throws UnauthorizedAccessException {
        PropertyManager.getInstance().setIsSold(property, realtor);
        assertTrue("Property should be marked as sold", property.getIsSold());
    }

    @Test(expected = UnauthorizedAccessException.class)
    public void testSetIsSoldWithUnauthorizedRealtor() throws UnauthorizedAccessException {
        Realtor unauthorizedRealtor = new Realtor("R789");
        PropertyManager.getInstance().setIsSold(property, unauthorizedRealtor);
    }

    // 3. בדיקות isWithinRadius
    @Test
    public void testIsWithinRadius() {
        // נכס במרחק 2 יחידות
        Property otherProperty = new Property(
                new Property.PropertyInfo(
                        new Address(new int[]{2, 3}),
                        80.0
                ),
                800000,
                false,
                seller,
                realtor
        );

        assertTrue("Property should be within radius 3", property.isWithinRadius(otherProperty, 3));
        assertFalse("Property should not be within radius 1", property.isWithinRadius(otherProperty, 1));
    }

    // 4. בדיקת toString
    @Test
    public void testToString() {
        String expected = address + "|" + SQUARE_METERS + "|" + PRICE + "|false|" + seller + "|" + realtor;
        assertEquals("toString should match expected format", expected, property.toString());
    }

    // 5. בדיקות Observer (update)
    @Test
    public void testUpdateWithAuthorizedRealtor() {
        int newPrice = 1200000;
        property.update(newPrice + "|" + realtor.getId());
        assertEquals("Price should be updated", newPrice, property.getPrice());
    }

    @Test
    public void testUpdateWithUnauthorizedRealtor() {
        int originalPrice = property.getPrice();
        property.update(1200000 + "|R789");
        assertEquals("Price should not change", originalPrice, property.getPrice());
    }

    // 6. בדיקות מקרי קצה
    @Test
    public void testZeroPriceProperty() {
        Property zeroPrice = new Property(new Property.PropertyInfo(address, SQUARE_METERS), 0, false, seller, realtor);
        assertEquals("Should allow zero price", 0, zeroPrice.getPrice());
    }

    @Test
    public void testMinimalSquareMeters() {
        Property smallProperty = new Property(new Property.PropertyInfo(address, 0.1), PRICE, false, seller, realtor);
        assertEquals("Should allow small area", 0.1, smallProperty.getInfo().squareMeters(), 0.001);
    }

    // 7. בדיקות נוספות
    @Test
    public void testDistanceCalculation() {
        Property property1 = new Property(new Property.PropertyInfo(new Address(new int[]{0, 0}), SQUARE_METERS), PRICE, false, seller, realtor);
        Property property2 = new Property(new Property.PropertyInfo(new Address(new int[]{3, 4}), SQUARE_METERS), PRICE, false, seller, realtor);

        assertTrue("Should be within radius 7", property1.isWithinRadius(property2, 7));
        assertTrue("Should be within radius exactly", property1.isWithinRadius(property2, 7));
        assertFalse("Should not be within radius 6", property1.isWithinRadius(property2, 6));
    }
}
