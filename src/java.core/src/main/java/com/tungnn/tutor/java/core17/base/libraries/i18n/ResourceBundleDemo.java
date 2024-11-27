package com.tungnn.tutor.java.core17.base.libraries.i18n;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleDemo {

    public static void main(String[] args) {
        Locale locale = new Locale("en", "US");
        ResourceBundle resourceBundle = ResourceBundle.getBundle("i18n/message", locale);

        System.out.println(resourceBundle.getString("greeting"));
    }
}
