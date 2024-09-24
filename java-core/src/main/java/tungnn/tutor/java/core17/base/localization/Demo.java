package tungnn.tutor.java.core17.base.localization;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Demo {

    public static void main(String[] args) {
        Locale locale = new Locale("en", "US");
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", locale);
        LocalDate localDate = LocalDate.now();

        System.out.println(dateTimeFormatter.format(localDate));

        DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, locale);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", locale);

    }
}
