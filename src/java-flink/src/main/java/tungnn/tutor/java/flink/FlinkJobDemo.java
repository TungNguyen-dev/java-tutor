package tungnn.tutor.java.flink;

import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import java.util.List;

public class FlinkJobDemo {

  private static final List<Person> PEOPLE =
      List.of(new Person("Fred", 35), new Person("Wilma", 35), new Person("Pebbles", 2));

  static void main() throws Exception {
    // Flink env
    final StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

    // Source
    DataStream<Person> source = env.fromData(PEOPLE).filter(person -> person.age >= 18);

    // Sink
    source.print();

    // Execute
    env.execute();
  }

  public record Person(String name, int age) {}
}
