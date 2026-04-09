package ua.ukma.edu.authorization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AuthorizationService {
    private final Map<String, User> users = new HashMap<>();

    public AuthorizationService() {
        users.put("user", new User("user", "us111", Roles.USER));
        users.put("manager", new User("manager", "man222", Roles.MANAGER));
        users.put("admin", new User("admin", "adm333", Roles.ADMIN));
    }
    public Optional<User> login(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            return Optional.empty();
        }
        if (user.isBlocked()) {
            System.out.println("Користувач заблокований");
            return Optional.empty();
        }
        if (user.getPassword().equals(password)) {
            return Optional.of(user);
        }
        return Optional.empty();
    }

    public void addUser(String username, String password, Roles role) {
        users.put(username, new User(username, password, role));
    }
    public void removeUser(String username) {
        users.remove(username);
    }
    public void changeUser(String username, Roles role) {
        User user = users.get(username);
        if (user != null) {
            user.setRole(role);
        }
    }
    public void blockUser(String username) {
        User user = users.get(username);
        if (user != null) {
            user.setBlocked(true);
        }
    }
    public void unblockUser(String username) {
        User user = users.get(username);
        if (user != null) {
            user.setBlocked(false);
        }
    }
    public Collection<User> getAllUsers() {
        return users.values();
    }
}