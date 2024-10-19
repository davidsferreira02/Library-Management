package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto { }
    @BeforeEach
    void setUp() {
    }
    @Test
    void ensureNameNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(null,validBio, null));
    }

    @Test
    void ensureBioNotNull(){
        assertThrows(IllegalArgumentException.class, () -> new Author(validName,null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName,validBio, null);

        assertThrows(StaleObjectStateException.class, () -> subject.applyPatch(999, request));
    }

    @Test
    void testCreateAuthorWithoutPhoto() {
        Author author = new Author(validName, validBio, null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testCreateAuthorRequestWithPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, "photoTest.jpg");
        Author author = new Author(request.getName(), request.getBio(), "photoTest.jpg");
        assertNotNull(author);
        assertEquals(request.getPhotoURI(), author.getPhoto().getPhotoFile());
    }

    @Test
    void testCreateAuthorRequestWithoutPhoto() {
        CreateAuthorRequest request = new CreateAuthorRequest(validName, validBio, null, null);
        Author author = new Author(request.getName(), request.getBio(), null);
        assertNotNull(author);
        assertNull(author.getPhoto());
    }

    @Test
    void testEntityWithPhotoSetPhotoInternalWithValidURI() {
        EntityWithPhoto entity = new EntityWithPhotoImpl();
        String validPhotoURI = "photoTest.jpg";
        entity.setPhoto(validPhotoURI);
        assertNotNull(entity.getPhoto());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Author author = new Author(validName, validBio, null);
        assertNull(author.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        Photo photo = author.getPhoto();
        assertNotNull(photo);
        assertEquals("photoTest.jpg", photo.getPhotoFile());
    }

   // unitario caixa opaca

    @Test
    void ensureName() {
        Author author = new Author(validName, validBio, null);
        assertEquals("João Alberto", author.getName());
    }

    @Test
    void ensureBio() {
        Author author = new Author(validName, validBio, null);
        assertEquals("O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.", author.getBio());
    }

    @Test
    void ensureAuthorCanBeUpdated() {
        Author author = new Author(validName, validBio, null);
        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest("Nova Bio", "Novo Nome", null, null);
        author.applyPatch(author.getVersion(), updateRequest);
        assertEquals("Novo Nome", author.getName());
        assertEquals("Nova Bio", author.getBio());
    }

    @Test
    void testApplyPatchWithNullFields() {
        Author author = new Author(validName, validBio, null);

        UpdateAuthorRequest updateRequest = new UpdateAuthorRequest(null, null, null, null);
        long currentVersion = author.getVersion();

        author.applyPatch(currentVersion, updateRequest);

        // Certifique-se de que nada foi alterado
        assertEquals(validName, author.getName());
        assertEquals(validBio, author.getBio());
    }


    //caixa branca

    @Test
    void testSetNameWithInvalidInput() {
        Author author = new Author(validName, validBio, null);

        assertThrows(IllegalArgumentException.class, () -> author.setName(null));
        assertThrows(IllegalArgumentException.class, () -> author.setName(""));
    }

    @Test
    void testRemovePhotoWithCorrectVersion() {
        Author author = new Author(validName, validBio, "photoTest.jpg");
        long currentVersion = author.getVersion();

        author.removePhoto(currentVersion);
        assertNull(author.getPhoto());
    }
}

