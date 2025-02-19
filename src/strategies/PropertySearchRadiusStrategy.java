package strategies;

import realestate.property.Property;

import java.util.List;

public interface PropertySearchRadiusStrategy<T> {
    T search(List<Property> properties, Property reference, int radius);
}
