package strategies;

import realestate.property.Property;

import java.util.ArrayList;
import java.util.List;

public class PropertyPriceCompareSearch implements PropertySearchRadiusStrategy<List<Property>> {
    private final CompareType comparisonType;
    private static final double PRICE_EPSILON = 0.01; // For floating point comparison

    public PropertyPriceCompareSearch(CompareType comparisonType) {
        this.comparisonType = comparisonType;
    }

    @Override
    public List<Property> search(List<Property> properties, Property reference, int radius) {
        double referencePricePerSqm = reference.getPrice() / reference.getInfo().squareMeters();
        List<Property> result = new ArrayList<>();
        for (Property property : properties) {
            // מחשב את המרחק מהכתובת
            if (property.isWithinRadius(reference, radius) && matchesPriceCriteria(property, referencePricePerSqm)) {
                result.add(property);
            }
        }

        return result;
    }

    private boolean matchesPriceCriteria(Property property, double referencePricePerSqm) {
        double propertyPricePerSqm = property.getPrice() / property.getInfo().squareMeters();

        return switch (comparisonType) {
            case HIGHER -> propertyPricePerSqm > referencePricePerSqm;
            case LOWER -> propertyPricePerSqm < referencePricePerSqm;
            case EQUAL -> propertyPricePerSqm == referencePricePerSqm;
            case NEAR_EQUAL -> Math.abs(propertyPricePerSqm - referencePricePerSqm) < PRICE_EPSILON;
        };
    }
}

