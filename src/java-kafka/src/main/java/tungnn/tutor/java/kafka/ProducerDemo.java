package tungnn.tutor.java.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerDemo {

  private static final String BOOTSTRAP_SERVER = "localhost:9092";
  private static final String TOPIC = "topic-demo";

  static void main(String[] args) {
    Properties props = new Properties();
    props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVER);
    props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
    props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

    try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
      String key = args[0];
      String value = args[1];
      ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, key, value);

      // Publish record
      producer.send(
          record,
          (metadata, exception) -> {
            if (exception == null) {
              System.out.printf(
                  "Sent record to topic=%s partition=%d offset=%d%n",
                  metadata.topic(), metadata.partition(), metadata.offset());
            } else {
              System.err.println(exception.getMessage());
            }
          });
    }
  }
}
