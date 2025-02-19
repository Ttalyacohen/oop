package strategies;

import realestate.property.Property;

import java.util.List;

public class PropertyAverageSearch implements PropertySearchRadiusStrategy<Double>{
    @Override
    public Double search(List<Property> properties, Property reference, int radius) {
        double sum=0;//סוכם את המחיר למר
        int count=0;//מונה את כמות הבתים
        for (Property property : properties) {
            if ( property.isWithinRadius(reference, radius)) {
                sum=sum+ (property.getPrice()/ property.getInfo().squareMeters());
                count++;
            }
        }

        return sum/count;
    }
}
