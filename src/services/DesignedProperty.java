package services;

import realestate.property.Property;

public class DesignedProperty extends ServicedProperty {
    public static final int COST = 3000;
    public static final String NAME = "Interior Design";

    public DesignedProperty(Property property) {
        super(property);
    }

    @Override
    protected int getServiceCost() {
        return COST;
    }

    @Override
    protected String getServiceName() {
        return NAME;
    }
}
