package tungnn.tutor.java.ast.docker;

import com.github.jimschubert.docker.ast.DockerInstruction;
import java.nio.file.Path;
import java.util.List;

public interface DockerfileParser {

  List<DockerInstruction> parse(Path dockerfile);
}
