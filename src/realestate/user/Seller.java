package realestate.user;

import exceptions.UnauthorizedAccessException;
import observer.Observer;
import observer.Subject;
import realestate.property.PropertyManager;
import realestate.property.Property;

import java.util.ArrayList;
import java.util.List;

public class Seller extends User implements CanDelete, Subject {
    private final List<Observer> observers = new ArrayList<>();

    public Seller(String id) {
        super(id);
    }

    public void sell(Property property, Realtor realtor) throws UnauthorizedAccessException {//כאשר מוכרים את הנכס, מודפת הודעה שהנכס נמכר והמוכר מוחק את הנכס מהמאגר
        if (realtor == null) {
            System.out.println("Error: Realtor provided is null.");
            return;
        }

        if (property.getRealtor() == null) {
            System.out.println("Error: Property does not have an assigned realtor.");
            return;
        }

        if (!realtor.equals(property.getRealtor())) {
            throw new UnauthorizedAccessException("Access Denied: This broker does not have permission to modify this property.");
        } else {
            System.out.print("the property in address: " + property.getInfo().address() + "belonging to the owner:" + property.getSeller() + "sold");
            deleteProperty(property);
        }
    }

    public void displaySellerProperties(PropertyManager pm) {
        List<Property> sellerProperties = pm.getPropertiesBySeller(this);
        if (sellerProperties.isEmpty()) {
            System.out.println("No properties found for this seller.");
        } else {
            System.out.println("Properties owned by the seller:");
            for (Property property : sellerProperties) {
                System.out.println(property);  // הדפסת פרטי הנכסים
            }
        }
    }


    @Override
    public String toString() {
        return super.getId();
    }

    @Override
    public void deleteProperty(Property property) {
        if (!property.getSeller().equals(this)) {
            System.out.println("Error! You do not own this property.");
            return;
        }
        PropertyManager propertyManager = PropertyManager.getInstance();  // גישה לסינגלטון
        propertyManager.update("delete|" + property.getInfo().address() + "|" + this.getId());//delete|address|user id
        System.out.println("Property at: " + property.getInfo().address() + " has been removed successfully by seller id: " + this.getId());
        this.notifyObservers("deleted" + property.getInfo().address());
    }

    @Override
    public void registerObserver(Observer o) {
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(String message) {
        for (Observer observer : observers) {
            observer.update(message);
        }
    }

}
