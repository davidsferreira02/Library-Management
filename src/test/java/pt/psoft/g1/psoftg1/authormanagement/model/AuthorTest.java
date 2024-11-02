package pt.psoft.g1.psoftg1.authormanagement.model;

import org.checkerframework.checker.units.qual.A;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;
import pt.psoft.g1.psoftg1.shared.model.Photo;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthorTest {
    private final String validName = "João Alberto";
    private final String validBio = "O João Alberto nasceu em Chaves e foi pedreiro a maior parte da sua vida.";

    private final UpdateAuthorRequest request = new UpdateAuthorRequest(validName, validBio, null, null);

    private static class EntityWithPhotoImpl extends EntityWithPhoto {
    }

    @BeforeEach
    void setUp() {
    }

    @Test
    void ensureNameNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(null, validBio, null));
    }

    @Test
    void ensureBioNotNull() {
        assertThrows(IllegalArgumentException.class, () -> new Author(validName, null, null));
    }

    @Test
    void whenVersionIsStaleItIsNotPossibleToPatch() {
        final var subject = new Author(validName, validBio, null);

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

    @Test
    void getTest() throws NoSuchMethodException {


        Author author = Mockito.spy(new Author("Initial Name", "Initial Bio", "initialPhoto"));
        UpdateAuthorRequest updateRequest = Mockito.mock(UpdateAuthorRequest.class);



        Mockito.when(updateRequest.getName()).thenReturn(null);
        Mockito.when(updateRequest.getBio()).thenReturn(null);
        Mockito.when(updateRequest.getPhotoURI()).thenReturn(null);
        author.applyPatch(author.getVersion(), updateRequest);

        Mockito.verify(author, Mockito.never()).setName(null);
        Mockito.verify(author, Mockito.never()).setBio(null);


        Mockito.when(updateRequest.getName()).thenReturn("New Name");
        Mockito.when(updateRequest.getBio()).thenReturn("New Bio");
        Mockito.when(updateRequest.getPhotoURI()).thenReturn("Initial Photo");


        author.applyPatch(author.getVersion(), updateRequest);

        Mockito.verify(author, Mockito.times(1)).setName("New Name");
        Mockito.verify(author, Mockito.times(1)).setBio("New Bio");


    }

    @Test
    void applyPatchTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Author author = new Author();
        UpdateAuthorRequest updateRequest = Mockito.mock(UpdateAuthorRequest.class);
        Mockito.when(updateRequest.getPhotoURI()).thenReturn("Initial Photo");
        Method setPhotoInternal = EntityWithPhoto.class.getDeclaredMethod("setPhotoInternal", String.class);
        setPhotoInternal.setAccessible(true);
        setPhotoInternal.invoke(author, "Initial Photo");
        assertEquals(author.getPhoto().getPhotoFile(), updateRequest.getPhotoURI());
        Mockito.when(updateRequest.getPhotoURI()).thenReturn(null);
        setPhotoInternal.invoke(author, "Initial Photo");
        assertEquals(author.getPhoto().getPhotoFile(), "Initial Photo");



    }


    @Test
    void removePhotoTest() {
        Author author = new Author("Initial Name", "Initial Bio", "initialPhoto");
        ReflectionTestUtils.setField(author, "version", 1L);
        author.removePhoto(1L);
        assertNull(author.getPhoto());
        Assertions.assertThrows(ConflictException.class, () -> {
            author.removePhoto(2L);
        });
    }

    @Test
    public void testProtectedConstructor() {
        Author author = new Author();
        assertNotNull(author);
    }

    @Test
    void getAuthorNumber() {
        Author author = new Author("Initial Name", "Initial Bio", "initialPhoto");
        ReflectionTestUtils.setField(author, "authorNumber", 1L);
        assertEquals(1L, author.getAuthorNumber());
    }

    @Test
    void getId() {
        Author author = new Author("Initial Name", "Initial Bio", "initialPhoto");
        ReflectionTestUtils.setField(author, "authorNumber", 1L);
        assertEquals(1L, author.getId());
    }



    @Test
    void getName(){
        Author author = new Author("Initial Name", "Initial Bio", "initialPhoto");
        assertEquals("Initial Name", author.getName());
    }

    @Test
    void getBio(){
        Author author = new Author("Initial Name", "Initial Bio", "initialPhoto");
        assertEquals("Initial Bio", author.getBio());
    }

    @Test
    public void getVersionTest() {
        Author author = new Author();
        ReflectionTestUtils.setField(author, "version", 1L);
        assertEquals(1L, author.getVersion());
    }

    @Test
    public void setPhotoInternalTest() throws Exception {
        Author author = new Author();
        Method setPhotoInternal = EntityWithPhoto.class.getDeclaredMethod("setPhotoInternal", String.class);
        setPhotoInternal.setAccessible(true);
        setPhotoInternal.invoke(author, "photoPath");

        assertEquals("photoPath", author.getPhoto().getPhotoFile());
    }

    @Test
    public void getGeneratedId(){
        Author author = new Author();
        author.setGeneratedId("1234");
        assertEquals("1234", author.getGeneratedId());
    }
}



