package tungnn.tutor.java.ast.java;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import java.nio.charset.StandardCharsets;

public final class JavaParserFactory {

  private JavaParserFactory() {}

  public static JavaParser createJavaParserVersion25() {
    var config = new ParserConfiguration();
    config.setLanguageLevel(ParserConfiguration.LanguageLevel.JAVA_25);
    config.setLexicalPreservationEnabled(true);
    config.setCharacterEncoding(StandardCharsets.UTF_8);

    return new JavaParser(config);
  }
}
