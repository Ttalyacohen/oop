package realestate.user;

import exceptions.PropertySoldException;
import exceptions.UnauthorizedAccessException;
import realestate.property.PropertyManager;
import realestate.property.Address;
import realestate.property.Property;

import java.util.Scanner;
import java.util.List;

public class Buyer extends User {
    private final Address[] addresses;

    public Buyer(String id) {
        super(id);
        this.addresses = null;
    }

    // פונקציה כדי שהקונה יצפה בכל הנכסים ויבחר נכס לקנייה
    public Property chooseProperties(Scanner scanner) throws UnauthorizedAccessException, PropertySoldException {
        PropertyManager propertyManager = PropertyManager.getInstance();

        System.out.println("Displaying all available properties:");
        // הצגת רק הנכסים הזמינים
        List<String> availableProperties = propertyManager.getAvailableProperties();
        if (availableProperties.isEmpty()) {
            System.out.println("No properties available for purchase.");
            return null;
        }

        // הצגת הנכסים הזמינים
        for (String property : availableProperties) {
            System.out.println(property);
        }

        System.out.print("Enter the index of the property you want to buy (-1 if you don't want to buy): ");
        int choice = scanner.nextInt();

        if (choice == -1) {
            System.out.println("You have chosen not to buy properties at this time.");
            return null;
        }

        // המרת האינדקס שהמשתמש הזין (שמתחיל מ-1) לאינדקס במערכת (שמתחיל מ-0)
        int index = choice - 1;
        Property chosenProperty = propertyManager.getPropertyByIndex(index);

        if (chosenProperty != null && !chosenProperty.getIsSold()) {
            System.out.println("You have selected: " + chosenProperty);
            return chosenProperty;
        } else {
            System.out.println("This property is either sold or doesn't exist.");
            return null;
        }
    }

    public void buyProperty(Property property, Scanner scanner) throws PropertySoldException, UnauthorizedAccessException {
        if (property == null)
            throw new IllegalArgumentException("Cannot buy a null property.");
        if (property.getIsSold())
            throw new PropertySoldException("This property has already been sold.");

        Realtor realtor = property.getRealtor();
        property = realtor.addServices(property); // הוספת עלות השירותים לעלות הכוללת

        System.out.println("\nProperty Details:");
        System.out.println("Buyer ID: " + this.getId());
        System.out.println("Property Address: " + property.getInfo().address());
        System.out.println("Total Cost: " + property.getPrice());
        System.out.println("Seller ID: " + property.getSeller().getId());

        System.out.print("\nWould you like to proceed with the purchase? (yes/no): ");

        if (scanner.next().equalsIgnoreCase("yes")) {
            realtor.creatingContract(property); // יצירת חוזה על ידי המתווך
            System.out.println("Purchase completed successfully!");
        } else {
            System.out.println("Purchase cancelled.");
        }
    }

    public Address[] getAdresses() {
        return addresses;
    }
}