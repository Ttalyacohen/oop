package services;

import realestate.property.Property;

public class MovedProperty extends ServicedProperty {
    public static final int COST = 5500;
    public static final String NAME = "Moving";

    public MovedProperty(Property property) {
        super(property);
    }

    protected int getServiceCost() {
        return COST;
    }

    @Override
    protected String getServiceName() {
        return NAME;
    }
}
