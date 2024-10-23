package pt.psoft.g1.psoftg1.usermanagement.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    void getID() {
        User user = new User();
        assertEquals(null, user.getId());
    }

    @Test
    void getCreatedAt() {
        User user = new User();
        assertEquals(null, user.getCreatedAt());
    }

    @Test
    void getModifiedAt() {
        User user = new User();
        assertEquals(null, user.getModifiedAt());
    }

    @Test
    void getCreatedBy() {
        User user = new User();
        assertEquals(null, user.getCreatedBy());
    }

    @Test
    void isEnabled() {
        User user = new User();
        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }

    @Test
    void isAccountNonExpired() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isAccountNonExpired());

        user.setEnabled(false);
        assertFalse(user.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isAccountNonLocked());

        user.setEnabled(false);
        assertFalse(user.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired() {
        User user = new User();
        user.setEnabled(true);
        assertTrue(user.isCredentialsNonExpired());

        user.setEnabled(false);


    }

    @Test
    void newUser() {
        String username = "testuser";
        String password = "TestPassword123";
        String name = "John Doe";
        String role = "ROLE_USER";

        User user = User.newUser(username, password, name);

        assertEquals(username, user.getUsername());
        assertTrue(new BCryptPasswordEncoder().matches(password, user.getPassword()));
        assertEquals(name, user.getName().toString());


        user = User.newUser(username, password, name, role);

        assertTrue(user.getAuthorities().stream().anyMatch(r -> r.getAuthority().equals(role)));

    }




}


