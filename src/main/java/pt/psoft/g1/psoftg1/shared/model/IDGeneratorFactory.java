package pt.psoft.g1.psoftg1.shared.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class IDGeneratorFactory {

    private static final Logger logger = LoggerFactory.getLogger(IDGeneratorFactory.class);

    private AlgorithmId algorithmId;

    @Value("${algorithm}")
    private String algorithmName;

    @Autowired
    public void init(ApplicationContext context) {
        try {
            this.algorithmId = context.getBean(algorithmName, AlgorithmId.class);
        } catch (Exception e) {
            logger.error("Algorithm bean with name '{}' not found", algorithmName, e);
            throw new IllegalStateException("Algorithm bean not found", e);
        }
    }

    public String generateId() {
        if (algorithmId == null) {
            throw new IllegalStateException("AlgorithmId is not initialized");
        }
        return algorithmId.generateId();
    }
}