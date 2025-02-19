package services;

import realestate.property.Property;

public abstract class ServicedProperty extends Property {
    protected final Property property;

    public ServicedProperty(Property property) {
        super(property.getInfo(), property.getPrice(), property.getIsSold(), property.getSeller(), property.getRealtor());
        this.property = property;
    }

    @Override
    public int getPrice() {
        return property.getPrice() + getServiceCost();
    }

    protected abstract int getServiceCost();

    protected abstract String getServiceName();

    @Override
    public String toString() {
        return property.toString() + "\n\t+ " + getServiceName() + " Service (Additional Cost: " + getServiceCost() + ")";
    }
}
