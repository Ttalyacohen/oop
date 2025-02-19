package services;

import realestate.property.Property;

public class CleanedProperty extends ServicedProperty {
    public static final int COST = 1500;
    public static final String NAME = "Pre-Move Cleaning";

    public CleanedProperty(Property property) {
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
