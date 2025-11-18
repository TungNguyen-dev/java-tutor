package tungnn.tutor.java.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class KafkaProducerExample {
  private static final String TOPIC = "my-kafka-topic";
  private static final String BOOTSTRAP_SERVERS = "localhost:9092";

  static void main() {
    // Configure producer properties
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    // Create the producer
    try (Producer<String, String> producer = new KafkaProducer<>(props)) {
      for (int i = 0; i < 10; i++) {
        // Create record
        String key = "Key" + i;
        String value = "Message " + i;
        ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);

        // Send a message
        System.out.println("Send message: " + record);
        producer.send(record);
      }
    }
  }
}
