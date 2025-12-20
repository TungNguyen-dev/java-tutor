package tungnn.tutor.java.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class ConsumerDemo {

  private static final String BOOTSTRAP_SERVER = "localhost:9092";
  private static final String GROUP_ID = "consumer-group-demo";
  private static final String TOPIC = "topic-demo";

  static void main() {
    Properties props = new Properties();
    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    props.put(ConsumerConfig.GROUP_ID_CONFIG, GROUP_ID);
    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());

    try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props)) {
      while (true) {
        // Subscribe
        consumer.subscribe(Collections.singletonList(TOPIC));

        // Poll records
        ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.ofMillis(100));

        // Handle result
        for (var record : consumerRecords) {
          System.out.printf(
              "Consumed record topic=%s partition=%d offset=%d key=%s value=%s%n",
              record.topic(), record.partition(), record.offset(), record.key(), record.value());

          // Conditional Terminate
          if (record.value().equals("exit")) {
            System.exit(0);
          }
        }
      }
    }
  }
}
