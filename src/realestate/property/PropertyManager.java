package realestate.property;

import exceptions.UnauthorizedAccessException;
import observer.Observer;
import realestate.user.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PropertyManager implements Observer {
    private static PropertyManager instance;

    // מתודה שמחזירה את האובייקט הקיים של PropertyManager
    public static PropertyManager getInstance() {
        return instance;
    }

    private final List<Property> propertyList;
    private final List<User> userList;

    private PropertyManager(String filePath) {
        this.propertyList = new ArrayList<>();
        this.userList = new ArrayList<>();
        this.BuildingFromFile(filePath);
    }

    public static PropertyManager createPropertyManager(String filePath) {
        if (instance == null) {
            instance = new PropertyManager(filePath);
        }
        return instance;
    }

    // פונקציה להדפיס נכסים בצורה רציפה עם מספר התוצאות הכולל
    public static void printProperties(List<Property> properties) {
        if (properties.isEmpty())
            System.out.println("No properties found.");
        else
            for (Property property : properties)
                System.out.println(property);  // הדפסת כל נכס בשורה חדשה
    }

    private void BuildingFromFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                Property property = createPropertyFromLine(line); // יוצרים נכס מתוך השורה
                propertyList.add(property);  // מוסיפים את הנכס לרשימה
                System.out.println("Added property at index: " + (propertyList.size()) + " - " + property);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // פונקציה לחיפוש נכס לפי אינדקס
    public Property getPropertyByIndex(int index) {
        if (index >= 0 && index < propertyList.size()) {
            return propertyList.get(index);
        } else {
            System.out.println("Invalid property index.");
            return null;
        }
    }

    // אפשרות להחזיר את כל המתווכים ברשימה
    public List<Realtor> getRealtors() {
        List<Realtor> realtors = new ArrayList<>();
        for (User user : userList)
            if (user instanceof Realtor)
                realtors.add((Realtor) user);
        return realtors;
    }

    // אפשרות להחזיר את כל המוכרים ברשימה
    public List<Seller> getSellers() {
        List<Seller> sellers = new ArrayList<>();
        for (User user : userList)
            if (user instanceof Seller)
                sellers.add((Seller) user);
        return sellers;
    }

    // חיפוש משתמש ברשימה של המשתמשים לפי id
    public User findUserById(String id) {
        for (User user : userList)
            if (user.getId().equals(id))
                return user;
        return null;
    }

    // פונקציה שמבצעת את הפיצול לכל שדה ומחזירה נכס חדש
    private Property createPropertyFromLine(String line)  {
        String[] parts = line.split("\\|"); // פיצול לפי |

        String[] addressParts = parts[0].substring(1, parts[0].length() - 1).split(",");
        int[] addressArr = new int[addressParts.length];
        for (int i = 0; i < addressParts.length; i++)
            addressArr[i] = Integer.parseInt(addressParts[i].trim());
        Address address = new Address(addressArr); // יצירת אובייקט Address
        double squareMeters = Integer.parseInt(parts[1]);
        int price = Integer.parseInt(parts[2]);
        boolean isSold = Boolean.parseBoolean(parts[3].trim());
        String tempSeller = parts[4].trim();
        String tempRealtor = parts[5].trim();
        // חיפוש המוכר והמתווך
        Seller seller = (Seller) findUserById(tempSeller);  // חיפוש המוכר
        Realtor realtor = (Realtor) findUserById(tempRealtor); // חיפוש המתווך

        if (seller == null) {
            seller = (Seller) UserFactory.createUser(UserType.SELLER, tempSeller);
            userList.add(seller);
        }
        if (realtor == null) {
            realtor = (Realtor) UserFactory.createUser(UserType.REALTOR, tempRealtor);
            userList.add(realtor);
        }
        Property p = new Property(
                new Property.PropertyInfo(address, squareMeters),
                price, isSold,
                seller, realtor
        );//יצירת אובייקט נכס חדש
        linkRealtorToSeller(seller, realtor);//שיוך המתווך למוכר
        return p;
    }


    private void setPrice(Property p, int newPrice) {
        p.price = newPrice;
    }

    public void setIsSold(Property p, Realtor realtor) throws UnauthorizedAccessException {//לסמן שהנכס נמכר
        // בודק אם המתווך הוא המתווך המוסמך לשנות את הסטטוס של הנכס
        if (!realtor.equals(p.getRealtor())) {
            // אם המתווך לא שייך לנכס, זורק שגיאה
            throw new UnauthorizedAccessException("Access Denied: This broker does not have permission to modify this property.");
        } else {
            // אם המתווך הוא המתווך המוסמך, משנה את הסטטוס של הנכס
            p.isSold = true;
        }

    }

    // פונקציה להדפסת כל הנכסים עם אינדקס
    public List<Property> getAllProperties() {
        // מחזיר את רשימת הנכסים
        return propertyList.isEmpty() ? null : propertyList;  // מחזיר null במקרה שאין נכסים
    }

    // פונקציה להחזרת רשימה רק של נכסים שלא נמכרו עדיין
    public List<String> getAvailableProperties() {
        List<String> availableProperties = new ArrayList<>();
        int index = 0;

        for (Property property : propertyList) {
            if (!property.getIsSold())
                availableProperties.add("(" + (index + 1) + ") " + property);
            index++;
        }
        return availableProperties;
    }


    //פונקציה לחיפוש משתמש מסוים ברשימה אם לא קיים תיצור לי אותו מחדש
    public User createUser(UserType userType, String id) {
        User user = findUserById(id);
        if (user == null) {
            user = UserFactory.createUser(userType, id);
        }
        return user;
    }

    // פונקציה להסרת נכס, תאפשר רק אם יש הרשאה
    public boolean removeProperty(Address address, User user) {
        if (user instanceof CanDelete) {
            Iterator<Property> iterator = propertyList.iterator();
            while (iterator.hasNext()) {
                Property p = iterator.next();
                if (p.getInfo().address().equals(address)) {
                    iterator.remove(); // שימוש ב-iterator.remove() במקום propertyList.remove()
                    writePropertiesToFile("newData.txt");
                    return true;
                }
            }
        }
        System.out.println("You do not have permission to delete this property.");
        return false;
    }

    private void linkRealtorToSeller(Seller seller, Realtor realtor) {//שיוך מתווך למוכר
        if (seller != null && realtor != null)
            seller.registerObserver(realtor);  // המתווך נרשם לקבלת עדכונים
    }

    public void addProperty(Property property) {
        if (property != null) {
            propertyList.add(property);
            linkRealtorToSeller(property.getSeller(), property.getRealtor());  //  שיוך המתווך למוכר
        }
    }

    private void writePropertiesToFile(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Property property : propertyList) {
                writer.write(property.toString());
                writer.newLine();
            }
            System.out.println("Properties have been written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing properties to file: " + e.getMessage());
        }
    }

    public List<Property> getPropertiesBySeller(Seller seller) {
        List<Property> sellerProperties = new ArrayList<>();

        for (Property property : propertyList)
            if (property.getSeller().equals(seller))
                sellerProperties.add(property);
        return sellerProperties;
    }

    public static void reset() {
        instance = null;
    }


    //delete or updateCost|address|user
    @Override
    public void update(String message) {
        String[] str = message.split("\\|");
        User user = findUserById(str[2]);
        Address address = new Address(str[1]);
        if (str[0].equals("delete")) {
            removeProperty(address, user);
        } else if (str[0].equals("updateCost")) {
            writePropertiesToFile("newData.txt");
        }
    }
}