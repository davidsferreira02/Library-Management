package pt.psoft.g1.psoftg1.authormanagement.model;

import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
    void testSetNameWithInvalidInput() {
        Author author = new Author(validName, validBio, null);

        assertThrows(IllegalArgumentException.class, () -> author.setName(null));
        assertThrows(IllegalArgumentException.class, () -> author.setName(""));
    }


    @Test
    void removePhotoWithMatchingVersion() {
        Author author = new Author("Author Name", "Author Bio", "photoURI");
        long currentVersion = author.getVersion();
        author.removePhoto(currentVersion);
        assertNull(author.getPhoto());
    }

    @Test
    void removePhotoWithNonMatchingVersion() {
        Author author = new Author("Author Name", "Author Bio", "photoURI");
        long currentVersion = author.getVersion();
        long wrongVersion = currentVersion + 1;
        assertThrows(ConflictException.class, () -> author.removePhoto(wrongVersion));
    }

    @Test
    void getId() {
        Author author = new Author(validName, validBio, null);
        assertEquals(null, author.getId());
    }

    @Test
    void getAuthorNumber() {
        Author author = new Author(validName, validBio, null);
        assertEquals(null, author.getAuthorNumber());
    }

    @Test
    void applyPatchWithoutNull(){
        Author author = new Author("Initial Name", "Initial Bio", "photo uri");
        UpdateAuthorRequest mockRequest = Mockito.mock(UpdateAuthorRequest.class);
        Mockito.when(mockRequest.getName()).thenReturn("Updated Name");
        Mockito.when(mockRequest.getBio()).thenReturn("new Bio");
        Mockito.when(mockRequest.getPhotoURI()).thenReturn("new photo uri");
        author.applyPatch(author.getVersion(), mockRequest);
        Mockito.verify(mockRequest, Mockito.times(2)).getName();
        Mockito.verify(mockRequest, Mockito.times(2)).getBio();
        Mockito.verify(mockRequest, Mockito.times(2)).getPhotoURI();

    }

    @Test
    void applyPatchWithNull(){
        Author author = new Author("Initial Name", "Initial Bio", "photo uri");
        UpdateAuthorRequest mockRequest = Mockito.mock(UpdateAuthorRequest.class);
        Mockito.when(mockRequest.getName()).thenReturn(null);
        Mockito.when(mockRequest.getBio()).thenReturn(null);
        Mockito.when(mockRequest.getPhotoURI()).thenReturn(null);
        author.applyPatch(author.getVersion(), mockRequest);
        Mockito.verify(mockRequest, Mockito.times(1)).getName();
        Mockito.verify(mockRequest, Mockito.times(1)).getBio();
        Mockito.verify(mockRequest, Mockito.times(1)).getPhotoURI();

    }
}

