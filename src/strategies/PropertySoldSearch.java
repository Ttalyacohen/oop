package strategies;

import realestate.property.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertySoldSearch implements PropertySearchRadiusStrategy<List<Property>>{
    @Override
    public List<Property> search(List<Property> properties, Property reference, int radius) {
        List<Property> result = new ArrayList<>();
        for (Property property : properties) {
            if (property.getIsSold() && property.isWithinRadius(reference, radius)) {
                result.add(property);
            }
        }
        return result;
    }
}
