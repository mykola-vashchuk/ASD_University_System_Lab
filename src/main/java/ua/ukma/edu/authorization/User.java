package ua.ukma.edu.authorization;

public class User {
    private String username;
    private String password;
    private Roles role;
    private boolean blocked;

    public User(String username, String password, Roles role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.blocked = false;
    }

    public String getUsername() {
        return username;
    }
    public String getPassword() {
        return password;
    }
    public Roles getRole() {
        return role;
    }
    public boolean isBlocked() {
        return blocked;
    }
    public void setRole(Roles role) {
        this.role = role;
    }
    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}