package pt.psoft.g1.psoftg1.usermanagement.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import pt.psoft.g1.psoftg1.lendingmanagement.model.Lending;
import pt.psoft.g1.psoftg1.readermanagement.model.EmailAddress;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LibrarianTest {

    @Test
    void createLibrarianWithValidDetails() {
        Librarian librarian = Librarian.newLibrarian("username", "Password95", "John Doe");
        assertEquals("username", librarian.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches("Password95", librarian.getPassword()));
        assertEquals("John Doe", librarian.getName().toString());
        assertTrue(librarian.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("LIBRARIAN")));
    }
    @Test
    void ProtectedConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Librarian> constructor = Librarian.class.getDeclaredConstructor();


        constructor.setAccessible(true);


        Librarian librarian = constructor.newInstance();

        assertNotNull(librarian, "The librarian instance must not be null");
    }


}
