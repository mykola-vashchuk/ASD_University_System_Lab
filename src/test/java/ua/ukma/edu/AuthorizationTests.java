package ua.ukma.edu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ua.ukma.edu.authorization.AuthorizationService;
import ua.ukma.edu.authorization.Roles;
import ua.ukma.edu.authorization.User;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthorizationTests {
     private  AuthorizationService authorizationService;

        @BeforeEach
        void setUp() {
            authorizationService = new AuthorizationService();
        }
        @Test
        void successUserLogin(){
            Optional<User> user = authorizationService.login("user","us111");
            assertTrue(user.isPresent());
            assertEquals(Roles.USER, user.get().role());
        }
    @Test
    void successManagerLogin(){
        Optional<User> user = authorizationService.login("manager","man222");
        assertTrue(user.isPresent());
        assertEquals(Roles.MANAGER, user.get().role());
    }
    @Test
    void successAdminLogin(){
        Optional<User> user = authorizationService.login("admin","adm333");
        assertTrue(user.isPresent());
        assertEquals(Roles.ADMIN, user.get().role());
    }
        @Test
        void failAdminLogin(){
            Optional<User> user = authorizationService.login("admin","adm111");
            assertFalse(user.isPresent());
        }

        @Test
        void testUser(){
            authorizationService.addUser("username", "password",  Roles.USER);
            assertTrue(authorizationService.login("username", "password").isPresent());
        }
    @Test
    void testManager(){
        authorizationService.removeUser("username");
        assertFalse(authorizationService.login("username", "password").isPresent());
    }

    @ParameterizedTest
    @CsvSource({
            "user, us111, true",
            "manag, man222, false",
            "admin, adm333, true"
    })

    void testParameterized(String username, String password, boolean expectedResult){
            assertEquals(expectedResult, authorizationService.login(username, password).isPresent());
    }
}