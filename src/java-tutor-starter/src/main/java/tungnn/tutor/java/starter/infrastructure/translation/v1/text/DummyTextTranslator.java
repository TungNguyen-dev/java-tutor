package tungnn.tutor.java.starter.infrastructure.translation.v1.text;

public class DummyTextTranslator implements TextTranslator {

  @Override
  public TextTranslationResponse translate(TextTranslationRequest request) {
    var items =
        request.texts().stream()
            .peek(
                textUnit -> {
                  System.out.println("Text: " + textUnit.text());
                })
            .map(
                textUnit ->
                    new TextTranslationResponse.TextUnitTranslated(
                        textUnit.textId(), textUnit.text() + "_translated"))
            .toList();
    return new TextTranslationResponse(items);
  }
}
