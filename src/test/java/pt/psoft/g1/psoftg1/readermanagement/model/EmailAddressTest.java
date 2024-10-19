package pt.psoft.g1.psoftg1.readermanagement.model;


import org.junit.jupiter.api.Test;
import jakarta.validation.ConstraintViolationException;

import static org.junit.jupiter.api.Assertions.*;

public class EmailAddressTest {


    @Test
    void ensureEmailAddressCanBeCreatedWithValidEmail() {
        assertDoesNotThrow(() -> new EmailAddress("valid.email@example.com"));
    }
}
