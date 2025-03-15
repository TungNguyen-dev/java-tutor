package com.tungnn.tutor.aws.util;

import com.tungnn.tutor.aws.sfn.model.Choice;
import com.tungnn.tutor.aws.sfn.model.State;

public class Utils {

    public static String parseNextAttribute(State state) {
        String nextAttribute;
        if (state.hasNext()) {
            nextAttribute = "\"Next\": \"" + state.nextStateId() + "\"";
        } else {
            if (state instanceof Choice) {
                nextAttribute = "";
            } else {
                nextAttribute = "\"End\": true";
            }
        }

        return nextAttribute;
    }

    public static void replacePlaceholder(StringBuilder sb, String placeholder, String replacement) {
        int start = sb.indexOf(placeholder);
        while (start != -1) {
            sb.replace(start, start + placeholder.length(), replacement);
            start = sb.indexOf(placeholder, start + replacement.length());
        }
    }
}
