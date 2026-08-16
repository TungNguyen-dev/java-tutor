package tungnn.tutor.java.tool.translation.core.text;

import java.util.List;

public record TextTranslationResponse(List<TextUnitTranslated> translations) {

  public record TextUnitTranslated(String textId, String translatedText) {}
}
