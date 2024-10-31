package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.annotation.PostConstruct;

@Component
public class IDGeneratorFactory {

    private AlgorithmId algorithmId;

    @Value("${algorithm}")
    private String algorithmName;

    private final ApplicationContext applicationContext;

    @Autowired
    public IDGeneratorFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @PostConstruct
    public void init() {
            this.algorithmId = applicationContext.getBean(algorithmName, AlgorithmId.class);
    }

    public String generateId() {
        return algorithmId.generateId(algorithmName);
    }
}
