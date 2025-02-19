package realestate.user;

import realestate.property.Property;

public interface CanDelete extends CanSee {
    void deleteProperty(Property property);
}
