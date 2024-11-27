package com.tungnn.tutor.java.core17.base.libraries.i18n;

import java.util.Locale;

public class CreateLocale {

    public static void main(String[] args) {
        Locale locale1 = new Locale("en", "US");
        System.out.println(locale1.getDisplayLanguage());
    }
}
