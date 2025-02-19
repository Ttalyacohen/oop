package realestate.user;

import realestate.property.Property;

public interface CanEdit extends CanSee {
    // פונקציה לעדכון המחיר של הנכס
    boolean editPropertyPrice(Property property, int newPrice);
}
