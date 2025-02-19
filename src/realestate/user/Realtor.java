package realestate.user;

import services.*;
import exceptions.CannotAffordException;
import exceptions.PropertySoldException;
import exceptions.UnauthorizedAccessException;
import observer.Observer;
import observer.Subject;
import realestate.property.PropertyManager;
import realestate.property.Property;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Realtor extends User implements CanEdit, Observer, Subject {
    private final List<Observer> observers = new ArrayList<>();

    public Realtor(String id) {
        super(id);
    }

    @Override
    public String toString() {
        return super.getId();
    }

    public boolean checkIfCanAfford(Buyer buyer, Property property) throws CannotAffordException, PropertySoldException {
        boolean ans = false;
        // אם הנכס נמכר כבר, נזרוק שגיאה
        if (property.getIsSold()) {
            throw new PropertySoldException("This property has already been sold.");
        }
        return ans;
    }


    public Property addServices(Property property) {
        int[] ansServices = {0, 0, 0, 0};
        Scanner scanner = new Scanner(System.in); // יצירת אובייקט Scanner לקריאת קלט מהמשתמש
        String[] services = {
                CleanedProperty.NAME + " Service",
                DesignedProperty.NAME + " Service",
                GuaranteedProperty.NAME + " Service",
                MovedProperty.NAME + " Service"
        };
        int[] prices = {
                CleanedProperty.COST,
                DesignedProperty.COST,
                GuaranteedProperty.COST,
                MovedProperty.COST
        };
        System.out.println("Select which services you want to add to the property (0 = Exit):");
        for (int i = 0; i < services.length; i++)
            System.out.println((i + 1) + ": " + services[i] + " - Cost: $" + prices[i]);
        int choice;
        while (true) {
            choice = scanner.nextInt();
            ServicedProperty servicedProperty = switch (choice) {
                case 1 -> new CleanedProperty(property);
                case 2 -> new DesignedProperty(property);
                case 3 -> new GuaranteedProperty(property);
                case 4 -> new MovedProperty(property);
                default -> null;
            };
            if (choice == 0) break;
            if (servicedProperty == null)
                System.out.println("Please choose a valid service option.");
            else {
                System.out.println("Services so far: " + servicedProperty);
                property = servicedProperty;
            }
        }
        System.out.println("Your service choices have been recorded.");
        System.out.print("The cost of the property including the price of the added services is: " + property.getPrice() + "\n");//הדפסת המחיר הכולל אחרי התוספות שנוספו למחיר הנכס
        return property;
    }

    public void creatingContract(Property property) throws UnauthorizedAccessException {
        PropertyManager.getInstance().setIsSold(property, property.getRealtor()); // משנה את הסטטוס של הנכס לנמכר
        System.out.println("Property at: " + property.getInfo().address() + " - was sold by the realtor id " + this.getId() + "\n");
        PropertyManager.getInstance().setIsSold(property, property.getRealtor());//משנה את הסטטוס של הנכס לנמכר
        System.out.print("Property at: " + property.getInfo().address() + " - was sold by the realtor id " + this.getId());
    }

    // פונקציה לעדכון המחיר של הנכס
    @Override
    public boolean editPropertyPrice(Property property, int newPrice) {
        // עדכון לנכס שיעדכן את המחיר של הנכס
        property.update(newPrice + "|" + this.getId());
        PropertyManager propertyManager = PropertyManager.getInstance();  // גישה לסינגלטון
        propertyManager.update("updateCost|" + property.getInfo().address() + "|" + this.getId());//updateCost|address|user id
        return true;  // הצלחה
    }

    @Override
    public void update(String message) {
        System.out.println("Realtor " + getId() + " received update: " + message);
    }

    @Override
    public void registerObserver(Observer observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer o : observers) {
            o.update(message);
        }
    }
}

