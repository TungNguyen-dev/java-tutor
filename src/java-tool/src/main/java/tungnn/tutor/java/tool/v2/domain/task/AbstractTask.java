package tungnn.tutor.java.tool.v2.domain.task;

public abstract class AbstractTask<I, O> implements Task<I, O> {

  @Override
  public final O execute(I input) {
    try {
      return doExecute(input);
    } catch (Exception e) {
      throw new RuntimeException("Internal error in " + getName(), e);
    }
  }

  protected abstract O doExecute(I input) throws Exception;
}
