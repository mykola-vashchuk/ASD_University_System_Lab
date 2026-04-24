package ua.ukma.edu.authorization;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuthorizationService {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private final Map<String, User> users = new HashMap<>();

    public AuthorizationService() {
        users.put("user", new User("user", "us111", Roles.USER));
        users.put("manager", new User("manager", "man222", Roles.MANAGER));
        users.put("admin", new User("admin", "adm333", Roles.ADMIN));
    }
    public Optional<User> login(String username, String password) {
        User user = users.get(username);
        if (user == null) {
            logger.warn("Невірний username");
            return Optional.empty();
        }
        if (user.blocked()) {
            logger.warn("Спроба входу заблокованим користувачем");
            return Optional.empty();
        }
        if (user.password().equals(password)) {
            logger.info("Авторизація успішна");
            return Optional.of(user);
        }
        logger.warn("Невірний password");
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
            users.put(username, user.role(role));
        }
    }
    public void blockUser(String username) {
        User user = users.get(username);
        if (user != null) {
            users.put(username, user.blocked(true));
        }
    }
    public void unblockUser(String username) {
        User user = users.get(username);
        if (user != null) {
            users.put(username, user.blocked(false));
        }
    }
    public Collection<User> getAllUsers() {
        return users.values();
    }
}