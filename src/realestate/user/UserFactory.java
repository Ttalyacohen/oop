package realestate.user;

public class UserFactory {
    public static User createUser(UserType userType, String id) {
        return switch (userType) {
            case BUYER -> new Buyer(id);
            case SELLER -> new Seller(id); // <-- חייב להיות תואם לבנאי של Seller
            case REALTOR -> new Realtor(id);
        };
    }
}
