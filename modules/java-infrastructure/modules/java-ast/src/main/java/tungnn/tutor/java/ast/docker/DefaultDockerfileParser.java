package tungnn.tutor.java.ast.docker;

import com.github.jimschubert.docker.ast.DockerInstruction;
import com.github.jimschubert.docker.parser.ParserError;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * Default {@link DockerfileParser} implementation.
 *
 * <p>Reads a Dockerfile from the filesystem and delegates the actual parsing to {@code
 * com.github.jimschubert:docker-parser}, which returns the list of {@link DockerInstruction} nodes
 * (FROM, RUN, COPY, ENV, comments, ...).
 */
public class DefaultDockerfileParser implements DockerfileParser {

  private final com.github.jimschubert.docker.parser.DockerfileParser delegate =
      new com.github.jimschubert.docker.parser.DockerfileParser();

  @Override
  public List<DockerInstruction> parse(Path dockerfile) {
    Objects.requireNonNull(dockerfile, "dockerfile path must not be null");

    if (!Files.isRegularFile(dockerfile)) {
      throw new IllegalArgumentException(
          "Not a readable Dockerfile: " + dockerfile.toAbsolutePath());
    }

    try {
      return delegate.parseDockerfile(Files.newInputStream(dockerfile));
    } catch (IOException e) {
      // The interface method is not checked-exception aware, so wrap it.
      throw new UncheckedIOException(
          "Failed to read Dockerfile: " + dockerfile.toAbsolutePath(), e);
    } catch (ParserError e) {
      throw new RuntimeException(e);
    }
  }
}
