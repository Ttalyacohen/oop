import strategies.*;
import exceptions.PropertySoldException;
import exceptions.UnauthorizedAccessException;
import realestate.property.PropertyManager;
import realestate.property.Property;
import realestate.user.*;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws UnauthorizedAccessException, PropertySoldException {
        Scanner scanner = new Scanner(System.in);
        PropertyManager PM;

        try {
            PM = PropertyManager.createPropertyManager("C:\\Users\\user\\IdeaProjects\\oopFinal\\src\\data.txt");
        } catch (Exception e) {
            System.out.println("Error: Unable to load property data. " + e.getMessage());
            return;
        }

        System.out.println("Welcome to the Real Estate System");
        User user = loginOrRegister(scanner, PM);

        boolean exit = false;
        while (!exit) {
            if (user instanceof Buyer) {
                exit = handleBuyer((Buyer) user, PM, scanner);
            } else if (user instanceof Seller) {
                exit = handleSeller((Seller) user, PM, scanner);
            } else if (user instanceof Realtor) {
                exit = handleRealtor((Realtor) user, PM, scanner);
            }
        }
    }

    private static boolean handleRealtor(Realtor realtor, PropertyManager pm, Scanner scanner) {
        while (true) {
            System.out.println("### Realtor Menu ###");
            System.out.println("1. View all properties");
            System.out.println("2. Search properties by radius");
            System.out.println("3. Edit property (Change price)");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // הצגת כל הנכסים

                    PropertyManager.printProperties(pm.getAllProperties());
                    break;
                case 2:
                    // חיפוש נכסים לפי רדיוס
                    handlePropertySearch(pm, scanner);
                    break;
                case 3:
                    // המתווך בוחר נכס כדי לשנות את המחיר שלו
                    System.out.println("Enter the index of the property you want to edit:");
                    int propertyIndex = scanner.nextInt() - 1;
                    Property propertyToEdit = pm.getPropertyByIndex(propertyIndex);
                    if (propertyToEdit != null) {
                        System.out.println("Current price: " + propertyToEdit.getPrice());
                        System.out.print("Enter new price: ");
                        int newPrice = scanner.nextInt();
                        realtor.editPropertyPrice(propertyToEdit, newPrice);  // קריאה לפונקציה CreatingContract
                        System.out.println("Property price updated successfully.");
                    } else
                        System.out.println("Invalid property selection.");
                    break;
                case 4:
                    // יציאה מהמערכת
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    scanner.nextLine(); // ניקוי buffer
                    String confirmation = scanner.nextLine().toLowerCase();
                    if (confirmation.equals("yes")) {
                        System.out.println("Thank you for using our system. Goodbye!");
                        System.exit(0);
                    }
                    break;
            }
        }
    }


    private static boolean handleSeller(Seller seller, PropertyManager pm, Scanner scanner) {
        while (true) {
            System.out.println("### Seller Menu ###");
            System.out.println("1. View your properties");
            System.out.println("2. Search properties by radius");
            System.out.println("3. Delete a property");
            System.out.println("4. View all properties");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    PropertyManager.printProperties(pm.getPropertiesBySeller(seller));
                    break;
                case 2:
                    // חיפוש נכסים לפי רדיוס
                    handlePropertySearch(pm, scanner);
                    break;
                case 3:
                    // המוכר בוחר נכס כדי למכור אותו
                    System.out.println("Enter the index of the property you want to delete:");
                    int propertyIndex = scanner.nextInt() - 1;
                    Property propertyToDelete = pm.getPropertyByIndex(propertyIndex);
                    if (propertyToDelete == null) {
                        try {
                            seller.sell(propertyToDelete, null);  // המוכר מוכר נכס
                            System.out.println("The property is null.");
                        } catch (UnauthorizedAccessException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        seller.deleteProperty(propertyToDelete);
                        System.out.println("Invalid property selection.");
                    }
                    break;
                case 4:
                    // הצגת כל הנכסים
                    PropertyManager.printProperties(pm.getAllProperties());
                    break;
                case 0:
                    // יציאה מהמערכת
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    scanner.nextLine(); // ניקוי buffer
                    String confirmation = scanner.nextLine().toLowerCase();
                    if (confirmation.equals("yes")) {
                        System.out.println("Thank you for using our system. Goodbye!");
                        System.exit(0);
                    }
                    break;
            }
        }
    }

    private static boolean handleBuyer(Buyer buyer, PropertyManager pm, Scanner scanner) throws UnauthorizedAccessException, PropertySoldException {
        while (true) {
            System.out.println("### Buyer Menu ###");
            System.out.println("1. View available properties");
            System.out.println("2. Search properties by radius");
            System.out.println("3. Buy a property");
            System.out.println("4. View all properties");
            System.out.println("0. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    // הצגת נכסים זמינים בלבד
                    List<String> availableProperties = pm.getAvailableProperties();
                    if (availableProperties.isEmpty()) {
                        System.out.println("No available properties.");
                    } else {
                        System.out.println("Available Properties:");
                        for (String property : availableProperties) {
                            System.out.println(property);
                        }
                    }
                    break;
                case 2:
                    // חיפוש נכסים לפי רדיוס
                    handlePropertySearch(pm, scanner);
                    break;
                case 3:
                    // קנייה של נכס
                    Property chosenProperty = buyer.chooseProperties(scanner);
                    if (chosenProperty != null) {
                        try {
                            buyer.buyProperty(chosenProperty, scanner); // העברת scanner כפרמטר
                        } catch (PropertySoldException | UnauthorizedAccessException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;
                case 4:
                    // הצגת כל הנכסים
                    List<Property> AllProperties = pm.getAllProperties();
                    for (int i = 0; i < AllProperties.size(); i++) {
                        System.out.println((i + 1) + ". " + AllProperties.get(i));
                    }
                    break;

                case 0:
                    // יציאה מהמערכת
                    System.out.print("Are you sure you want to exit? (yes/no): ");
                    scanner.nextLine(); // ניקוי buffer
                    String confirmation = scanner.nextLine().toLowerCase();
                    if (confirmation.equals("yes")) {
                        System.out.println("Thank you for using our system. Goodbye!");
                        System.exit(0);
                    }
                    break;
            }
        }
    }

    private static User loginOrRegister(Scanner scanner, PropertyManager pm) {
        System.out.println("Enter your user ID to login or type 'register' to create a new user:");
        String userInput = scanner.nextLine();

        // אם המשתמש מזין מזהה שמתאים ללקוח קונה (B123 וכו'), הוא יירשם כקונה
        if (userInput.matches("B\\d{3,}")) {
            System.out.println("Recognized as Buyer. Logging in...");
            return new Buyer(userInput);
        }

        // אם המשתמש בוחר להירשם
        if (userInput.equalsIgnoreCase("register")) {
            System.out.println("Registering a new user...");
            System.out.print("Enter your user ID: ");
            String userId = scanner.nextLine();
            System.out.println("Choose user type: 1. Buyer 2. Seller 3. Realtor");
            int userTypeChoice = scanner.nextInt();
            scanner.nextLine(); // לצריכת תו מעבר שורה

            return switch (userTypeChoice) {
                case 1 -> new Buyer(userId);
                case 2 -> new Seller(userId);
                case 3 -> new Realtor(userId);
                default -> null;
            };
        }

        // במקרה של לוגין, נבדוק אם המשתמש קיים
        User existingUser = pm.findUserById(userInput);
        if (existingUser != null) {
            System.out.println("Login successful.");
            return existingUser;
        } else {
            System.out.println("User not found. Please register.");
            return null;
        }
    }


    private static void handlePropertySearch(PropertyManager PM, Scanner scanner) {
        System.out.println("### Property Search Menu ###");
        System.out.println("1. Average price per square meter in radius");
        System.out.println("2. Sold properties in radius");
        System.out.println("3. Available properties in radius");
        System.out.println("4. Compare properties by price per square meter");
        System.out.println("0. Return to main menu");

        int choice = scanner.nextInt();

        if (choice == 0) return;

        int index = -1;
        boolean validIndex = false;

        // לוודא שהאינדקס תקין
        while (!validIndex) {
            System.out.println("Enter property index: ");
            index = scanner.nextInt() - 1;  // מאפסים את האינדקס למספר ממשי
            Property reference = PM.getPropertyByIndex(index);

            if (reference != null) {
                validIndex = true;  // אם מצאנו נכס, האינדקס תקין
            } else {
                System.out.println("Please try again.");
            }
        }

        System.out.println("Enter search radius: ");
        int radius = scanner.nextInt();

        Property reference = PM.getPropertyByIndex(index);  // עכשיו האינדקס תקין

        switch (choice) {
            case 1:
                double avgPrice = new PropertyAverageSearch().search(PM.getAllProperties(), reference, radius);
                System.out.println("Average price per square meter: " + avgPrice);
                break;
            case 2:
                List<Property> soldProperties = new PropertySoldSearch().search(PM.getAllProperties(), reference, radius);
                System.out.println("Sold properties:");
                PropertyManager.printProperties(soldProperties);  // הדפסת נכסים שמולאו לפי רדיוס בשורות נפרדות
                break;
            case 3:
                List<Property> availableProperties = new PropertyToBuySearch().search(PM.getAllProperties(), reference, radius);
                System.out.println("Available properties:");
                PropertyManager.printProperties(availableProperties);  // הדפסת נכסים זמינים לפי רדיוס בשורות נפרדות
                break;
            case 4:
                System.out.println("Choose comparison type: 1. Higher 2. Lower 3. Equal 4. Nearly-Equal");
                int compChoice = scanner.nextInt();
                List<Property> comparedProperties = new PropertyPriceCompareSearch(
                        CompareType.values()[compChoice - 1]
                ).search(PM.getAllProperties(), reference, radius);
                System.out.println("Matching properties:");
                PropertyManager.printProperties(comparedProperties);  // הדפסת נכסים שעברו השוואה לפי רדיוס בשורות נפרדות
                break;
        }
    }
}

