package ua.ukma.edu.authorization;

public record User (
         String username,
         String password,
         Roles role,
         boolean blocked
)
{
    public User(String username, String password, Roles role) {
        this(username, password, role, false);
    }
    public User role(Roles role) {
        return new User(username, password, role, blocked);
    }
    public User blocked(boolean blocked) {
        return new User(username, password, role, blocked);
    }
}