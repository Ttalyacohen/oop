package realestate.property;

public class Address {
    private final int[] address;

    public Address(int[] address) {
        this.address = address;
    }

    public Address(String ad) {
        String[] str = ad.substring(1, ad.length() - 1).split(",");
        this.address = new int[str.length];
        for (int i = 0; i < str.length; i++) {
            this.address[i] = Integer.parseInt(str[i]);
        }
    }

    // בדיקה אם הנכס בתוך הרדיוס
    public boolean isWithinRadius(Address reference, int radius) {
        int distance = Math.abs(this.address[0] - reference.address[0]) + Math.abs(this.address[1] - reference.address[1]);
        return distance <= radius;
    }

    @Override
    public String toString() {
        String answer = "(";
        for (int i = 0; i < address.length; i++) {
            if (i != address.length - 1) {
                answer = answer + address[i] + ",";
            } else
                answer = answer + address[i];
        }
        return answer + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address)) {
            return false;
        }
        return this.toString().equals(((Address) o).toString());
    }

}