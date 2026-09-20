package tungnn.tutor.java.tool.translation_old.core.text;

import java.util.List;

public record TextTranslationResponse(List<TextUnitTranslated> translations) {

  public record TextUnitTranslated(String textId, String translatedText) {}
}
