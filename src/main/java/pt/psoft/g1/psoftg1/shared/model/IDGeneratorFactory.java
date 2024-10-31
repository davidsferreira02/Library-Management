package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class IDGeneratorFactory {

    private AlgorithmId algorithmId;

    @Value("${algorithm}")
    private String algorithmName;

    @Autowired
    public void init(ApplicationContext context) {

        this.algorithmId = context.getBean(algorithmName, AlgorithmId.class);
    }

    public String generateId() {
        return algorithmId.generateId();
    }
}
