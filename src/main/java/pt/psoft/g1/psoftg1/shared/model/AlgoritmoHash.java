package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

@Component
public class AlgoritmoHash implements AlgorithmId {

    @Override
    public String generateId(String algorithmName) {
        return "";
    }
}
