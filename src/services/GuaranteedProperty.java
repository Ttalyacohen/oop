package services;

import realestate.property.Property;

public class GuaranteedProperty extends ServicedProperty {
    public static final int COST = 4000;
    public static final String NAME = "Evening Guaranty";

    public GuaranteedProperty(Property property) {
        super(property);
    }

    public int getServiceCost() {
        return COST;
    }

    @Override
    protected String getServiceName() {
        return NAME;
    }
}
