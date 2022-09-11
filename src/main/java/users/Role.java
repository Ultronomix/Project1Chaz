package users;

import java.util.Objects;

public class Role {
private String userId;
private String name;

    public Role(String userId, String role) {
        this.userId = userId;
        this.name = role;
    }

    public String getId() {
        return userId;
    }

    public void setId(String id) {
        this.userId = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role1 = (Role) o;
        return Objects.equals(userId, role1.userId) && Objects.equals(name, role1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, name);
    }

    @Override
    public String toString() {
        return "Role{" +
                "id='" + userId + '\'' +
                ", role='" + name + '\'' +
                '}';
    }
}
