package ua.ukma.edu.authorization;

public class User {
    private String username;
    private String password;
    private Roles role;

    public User(String username, String password, Roles role) {
        this.username = username;
        this.password = password;
        this.role = role;
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
}