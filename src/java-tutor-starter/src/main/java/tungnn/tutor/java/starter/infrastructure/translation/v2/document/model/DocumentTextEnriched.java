package tungnn.tutor.java.starter.infrastructure.translation.v2.document.model;

import tungnn.tutor.java.starter.infrastructure.translation.v2.document.DocumentText;

public record DocumentTextEnriched(DocumentText documentText, String text, Integer order) {}
