package realestate.property;

import observer.Observer;
import realestate.user.Realtor;
import realestate.user.Seller;

public class Property implements Observer {
    public record PropertyInfo(Address address, double squareMeters) {

        @Override
            public String toString() {
                return address().toString() + "|" + squareMeters();
            }
        }

    private final PropertyInfo propertyInfo;

    protected int price;
    protected boolean isSold;
    private final Seller seller;
    private final Realtor realtor;

    /**
     * Constructor for creating a new property listing.
     * When creating a new property, all attributes are initialized except for the
     * sale date (which defaults to false).
     **/
    public Property(PropertyInfo info, int price, boolean isSold, Seller seller, Realtor realtor) {
        this.propertyInfo = info;
        this.price = price;
        this.isSold = isSold;
        this.seller = seller;
        this.realtor = realtor;

        this.realtor.registerObserver(this);
    }

    public PropertyInfo getInfo() {
        return propertyInfo;
    }

    public int getPrice() {
        return price;
    }

    public boolean getIsSold() {
        return isSold;
    }

    public Seller getSeller() {
        return seller;
    }

    public Realtor getRealtor() {
        return realtor;
    }

    public boolean isWithinRadius(Property reference, int radius) {
        return this.propertyInfo.address().isWithinRadius(reference.getInfo().address(), radius);
    }

    // House(address|squareMeters|price|isSold|seller|realtor)
    @Override
    public String toString() {
        return getInfo().toString() +
                "|" + price +
                "|" + isSold +
                "|" + seller +
                "|" + realtor;
    }

    @Override
    public void update(String message) {
        int newPrice = Integer.parseInt(message.split("\\|")[0]);
        String realtorID = message.split("\\|")[1];

        if (realtorID.equals(getRealtor().getId())) {
            System.out.print("The price of the property :" + getInfo().address() + "updated from " + getPrice());
            this.price = newPrice;
            System.out.println("to " + getPrice());
        } else
            System.out.println("Error! You are not the realtor of the current property and therefore cannot update the price of the property.");
    }

}