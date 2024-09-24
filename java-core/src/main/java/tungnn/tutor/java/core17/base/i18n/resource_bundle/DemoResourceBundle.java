package tungnn.tutor.java.core17.base.i18n.resource_bundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class DemoResourceBundle {

    public static void main(String[] args) {
        Locale locale = new Locale("en", "UK");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("message", locale);

        for (String key : resourceBundle.keySet()) {
            System.out.println(key + ": " + resourceBundle.getString(key));
        }
    }
}
