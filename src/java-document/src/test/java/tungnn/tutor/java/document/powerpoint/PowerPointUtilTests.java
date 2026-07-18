package tungnn.tutor.java.document.powerpoint;

import java.io.IOException;
import tungnn.tutor.java.core.lib.io.resource.ResourceUtil;

public class PowerPointUtilTests {

  static void main() throws IOException {
    PowerPointUtilTests tests = new PowerPointUtilTests();
    tests.testCollectTextRuns();
  }

  void testCollectTextRuns() throws IOException {
    try (var document =
        PowerPointUtil.readPresentation(ResourceUtil.getResourceAsStream("SamplePPTX-All.pptx"))) {

      var textRuns = PowerPointUtil.collectTextRuns(document, text -> true);

      textRuns.forEach(
          (text, runs) -> {
            runs.forEach(System.out::println);
          });
    }
  }
}
