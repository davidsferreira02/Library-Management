package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.UUID;


@Component("algorithmHex")
public class AlgorithmHex implements AlgorithmId {

    @Override
    public String generateId() {


        return UUID.randomUUID().toString().replace("-", "").substring(0, 24);

    }

}

