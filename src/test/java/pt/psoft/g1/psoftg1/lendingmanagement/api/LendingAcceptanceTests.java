/*package pt.psoft.g1.psoftg1.lendingmanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pt.psoft.g1.psoftg1.auth.api.AuthApi;
import pt.psoft.g1.psoftg1.auth.api.AuthRequest;
import pt.psoft.g1.psoftg1.lendingmanagement.services.CreateLendingRequest;
import pt.psoft.g1.psoftg1.usermanagement.api.UserView;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class LendingAcceptanceTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private AuthApi authApi;

    private String authToken;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        AuthRequest authRequest = new AuthRequest("maria@gmail.com",
                "Mariaroberta!123");
        ResponseEntity<UserView> response = authApi.login(authRequest);
        List<String> tokenList = response.getHeaders().get("Authorization");
        if (tokenList != null && !tokenList.isEmpty()) {
            authToken = tokenList.get(0);
        }
    }

    @Test
    public void shouldCreateNewLendingAndReturnCreatedStatus() throws Exception {
        CreateLendingRequest createLendingRequest = new CreateLendingRequest();
        createLendingRequest.setIsbn("978-3-16-148410-0");
        createLendingRequest.setReaderNumber("reader-number-example");

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/lendings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", authToken)
                        .content(objectMapper.writeValueAsString(createLendingRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        String locationHeader = result.getResponse().getHeader("Location");
        String eTagHeader = result.getResponse().getHeader("ETag");

        assertNotNull(locationHeader, "A URI de localização deve ser definida");
        assertNotNull(eTagHeader, "O cabeçalho ETag deve estar presente");

        // Conteúdo da resposta
        String resultContent = result.getResponse().getContentAsString();
        assertNotNull(resultContent, "O conteúdo da resposta não deve ser nulo");
    }
}*/