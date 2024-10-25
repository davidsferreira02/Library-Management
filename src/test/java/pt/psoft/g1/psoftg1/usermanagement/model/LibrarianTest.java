package pt.psoft.g1.psoftg1.usermanagement.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class LibrarianTest {
    @Test
    public void newLibrarianShouldSetAttributesCorrectly() {

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Librarian librarian = Librarian.newLibrarian("Ajdjdf123", "Password132", "João");


        assertEquals("João", librarian.getName().toString());


        assertEquals("Ajdjdf123", librarian.getUsername());

        assertTrue(passwordEncoder.matches("Password132", librarian.getPassword()));


        assertTrue(librarian.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals(Role.LIBRARIAN)));
    }

    @Test
    void ProtectedConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Librarian> constructor = Librarian.class.getDeclaredConstructor();


        constructor.setAccessible(true);


        Librarian librarian = constructor.newInstance();

        assertNotNull(librarian, "The librarian instance must not be null");
    }
}
