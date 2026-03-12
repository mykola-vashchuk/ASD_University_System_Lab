package ua.ukma.edu.authorization;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthorizationServiceDemo {
    private Map<String, User> users = new HashMap<>();

    public AuthorizationServiceDemo() {
        users.put("user", new User("user", "123", Roles.USER));
        users.put("manager", new User("manager", "456", Roles.MANAGER));
    }
    public Optional<User> login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}