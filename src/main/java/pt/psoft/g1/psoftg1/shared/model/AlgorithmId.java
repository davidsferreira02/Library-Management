package pt.psoft.g1.psoftg1.shared.model;

import org.checkerframework.checker.units.qual.A;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

public interface AlgorithmId {

    String generateId(String algorithmName);
}
