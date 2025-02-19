package tests;
import exceptions.UnauthorizedAccessException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import realestate.property.Address;
import realestate.property.Property;
import realestate.property.PropertyManager;
import realestate.user.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PropertyManagerTest {
    private PropertyManager propertyManager;
    private Seller seller;
    private Realtor realtor;

    @BeforeEach
    void setUp() {
        // יצירת קובץ טסט זמני
        String testFilePath = "test_properties.txt";
        try {
            // יצירת המשתמשים
            seller = new Seller("1111");
            realtor = new Realtor("2222");

            // יצירת תוכן הקובץ
            try (FileWriter writer = new FileWriter(testFilePath)) {
                writer.write("(1,2)|100|200000|false|1111|2222\n");
            }

            // יצירת PropertyManager
            propertyManager = PropertyManager.createPropertyManager(testFilePath);

            // הוספת המשתמשים למערכת באופן ישיר
            propertyManager.userList.add(seller);
            propertyManager.userList.add(realtor);

        } catch (IOException e) {
            fail("Could not create test file: " + e.getMessage());
        }
    }

    @Test
    void testFindExistingUser() {
        // בדיקת מציאת משתמש קיים
        User foundUser = propertyManager.findUserById("1111");
        assertNotNull(foundUser, "User should be found");
        assertEquals("1111", foundUser.getId());
    }

    @Test
    void testFindNonExistingUser() {
        // בדיקת מציאת משתמש לא קיים
        User foundUser = propertyManager.findUserById("9999");
        assertNull(foundUser, "Non-existing user should return null");
    }

    @Test
    void testGetProperty() {
        // בדיקת קבלת נכס קיים
        Property property = propertyManager.getPropertyByIndex(0);
        assertNotNull(property, "Property should exist");
        assertEquals("(1,2)", property.getInfo().address().toString());
        assertEquals(200000, property.getPrice());
    }


    @Test
    void testGetRealtors() {
        // בדיקת רשימת המתווכים
        List<Realtor> realtors = propertyManager.getRealtors();
        assertFalse(realtors.isEmpty(), "Should have realtors");
        assertEquals(1, realtors.size(), "Should have exactly one realtor");
        assertEquals("2222", realtors.get(0).getId(), "Realtor should have correct ID");
    }

    @Test
    void testGetSellers() {
        // בדיקת רשימת המוכרים
        List<Seller> sellers = propertyManager.getSellers();
        assertFalse(sellers.isEmpty(), "Should have sellers");
        assertEquals(1, sellers.size(), "Should have exactly one seller");
        assertEquals("1111", sellers.get(0).getId(), "Seller should have correct ID");
    }

    @AfterEach
    void tearDown() {
        // ניקוי קבצים זמניים
        new File("test_properties.txt").delete();
        new File("newData.txt").delete();
        PropertyManager.reset(); // אם קיימת מתודת reset
    }
}
