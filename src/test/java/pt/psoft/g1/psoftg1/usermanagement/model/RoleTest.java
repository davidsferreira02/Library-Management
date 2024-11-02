package pt.psoft.g1.psoftg1.usermanagement.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.security.core.GrantedAuthority;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

class RoleTest {

    @Test
    void testRoleAdminAuthority() {
        Role role = new Role(Role.ADMIN);
        assertEquals("ADMIN", role.getAuthority());
    }

    @Test
    void testRoleLibrarianAuthority() {
        Role role = new Role(Role.LIBRARIAN);
        assertEquals("LIBRARIAN", role.getAuthority());
    }

    @Test
    void testRoleReaderAuthority() {
        Role role = new Role(Role.READER);
        assertEquals("READER", role.getAuthority());
    }

    @Test
    void testCustomAuthority() {
        Role role = new Role("CUSTOM_ROLE");
        assertEquals("CUSTOM_ROLE", role.getAuthority());
    }

    @Test
    void testRoleImplementsGrantedAuthority() {
        Role role = new Role(Role.ADMIN);
        assertTrue(role instanceof GrantedAuthority);
    }

    @Test
    void testEqualityAndHashCode() {
        Role role1 = new Role(Role.ADMIN);
        Role role2 = new Role(Role.ADMIN);
        Role role3 = new Role("CUSTOM_ROLE");

        assertEquals(role1, role2, "Roles with the same authority should be equal");
        assertNotEquals(role1, role3, "Roles with different authorities should not be equal");

        assertEquals(role1.hashCode(), role2.hashCode(), "Hash codes should match for roles with the same authority");
        assertNotEquals(role1.hashCode(), role3.hashCode(), "Hash codes should not match for roles with different authorities");
    }

    @Test
    void testToString() {
        Role role = new Role(Role.ADMIN);
        assertEquals("ADMIN", role.getAuthority());
    }
}
