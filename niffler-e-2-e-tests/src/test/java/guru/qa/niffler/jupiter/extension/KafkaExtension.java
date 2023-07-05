package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.kafka.KafkaConsumerService;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class KafkaExtension implements SuiteExtension {

    private static final KafkaConsumerService kafkaConsumerService = new KafkaConsumerService();
    private static final ExecutorService es = Executors.newSingleThreadExecutor();

    @Override
    public void beforeSuite() {
        es.execute(new KafkaConsumerService());
        es.shutdown();
    }

    @Override
    public void afterSuite() {
        kafkaConsumerService.shutdown();
    }
}
