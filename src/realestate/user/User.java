package realestate.user;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class User implements CanSee {
    private String id;
    private static final Set<String> allID = new HashSet<>();

    protected User(String id) {
        if (allID.contains(id)) {
            System.out.print("There is already someone with this ID: " + id + "\n");
            return;
        }
        this.id = id;
        allID.add(id);
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof User user)
            return Objects.equals(user.id, this.id);
        return false;
    }
}
