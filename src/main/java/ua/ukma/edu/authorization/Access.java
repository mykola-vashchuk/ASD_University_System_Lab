package ua.ukma.edu.authorization;

import java.util.Optional;
import java.util.Scanner;

public class Access {
    public static void main(String[] args) {
    AuthorizationServiceDemo authorizationServiceDemo = new AuthorizationServiceDemo();
    Scanner scanner = new Scanner(System.in);

    System.out.println("Enter username: ");
    String username = scanner.next();
    System.out.println("Enter password: ");
    String password = scanner.next();

    Optional<User> userOptional = authorizationServiceDemo.login(username, password);
    if (userOptional.isPresent()) {
        User user = userOptional.get();
        System.out.println("Successfully logged in. Your name: " + user.getUsername() + ", your role: " + user.getRole());
        switch(user.getRole()) {
            case USER -> UserMenu();
            case MANAGER ->  ManagerMenu();
        }

    }
    else {
        System.out.println("Invalid username or password");
    }

    }
    private static void UserMenu() {
        System.out.println("Yor have access only to");
        System.out.println("1.Пошук");
        System.out.println("1.Звіт");
    }

    private static void ManagerMenu() {
        System.out.println("Yor have access to");
        System.out.println("1.Додати");
        System.out.println("2.Редагувати");
        System.out.println("3.Видалити");
        System.out.println("4.Пошук");
        System.out.println("5.Звіт");
    }
}