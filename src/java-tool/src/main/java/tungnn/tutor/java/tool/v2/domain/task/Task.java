package tungnn.tutor.java.tool.v2.domain.task;

public interface Task<I, O> {

  String getName();

  O execute(I input);
}
