package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.stereotype.Component;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component("AlgorithmHash")
public class AlgorithmHash implements AlgorithmId {

    @Override
    public String generateId(String businessId) {
        try {
            // Use SHA-1 to create a hash of the input
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            byte[] hash = digest.digest(businessId.getBytes());

            // Encode the hash in Base64 to make it alphanumeric
            String base64Hash = Base64.getUrlEncoder().encodeToString(hash);

            // Return the first 20 characters
            return base64Hash.substring(0, 20);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error: SHA-1 algorithm not found.", e);
        }
    }
}
