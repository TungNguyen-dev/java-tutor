package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

import java.util.List;

public record TextTranslationResponse(List<TextUnitTranslated> translations) {

  public record TextUnitTranslated(String textId, String translatedText) {}
}
