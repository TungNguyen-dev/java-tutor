package tungnn.tutor.java.ast.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import java.io.IOException;
import java.nio.file.Path;

public final class JavaParserUtil {

  private JavaParserUtil() {}

  public static CompilationUnit parseCompilationUnit(JavaParser parser, Path path) {
    try {
      return parser.parse(path).getResult().orElseThrow();
    } catch (IOException e) {
      throw new JavaParsingException("Failed to parse file: " + path.toString());
    }
  }
}
