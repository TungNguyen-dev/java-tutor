package tungnn.tutor.java.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.Properties;

public class KafkaConsumerExample {
  private static final String TOPIC = "my-kafka-topic";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";
  private static final String GROUP_ID = "my-group-id";

  static void main() {
    // Configure consumer properties
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    // Create the consumer
    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      consumer.subscribe(Collections.singletonList(TOPIC));

      // Poll for new messages
      Instant start = Instant.now();
      Duration duration = Duration.ofSeconds(10);
      while (start.isBefore(Instant.now().plus(duration))) {
        ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
        for (ConsumerRecord<String, String> record : records) {
          System.out.println("Received message: " + record);
        }
      }
    }
  }
}
