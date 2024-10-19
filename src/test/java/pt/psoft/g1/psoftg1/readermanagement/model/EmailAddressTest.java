package pt.psoft.g1.psoftg1.readermanagement.model;


import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static org.junit.jupiter.api.Assertions.*;

public class EmailAddressTest {


    @Test
    void ensureEmailAddressCanBeCreatedWithValidEmail() {
        assertDoesNotThrow(() -> new EmailAddress("valid.email@example.com"));
    }
    @Test
    void ProtectedConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<EmailAddress> constructor = EmailAddress.class.getDeclaredConstructor();


        constructor.setAccessible(true);


        EmailAddress emailAddress = constructor.newInstance();

        assertNotNull(emailAddress, "The EmailAddress instance must not be null");
    }
}
