package sample;


import javafx.scene.control.Alert;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*Проверка заполнения даты инцидента*/
public class OutputCheckerUtils {
    private static final String DATE_PATTERN = "dd.MM.yyyy";
    public static String format (String date)  {
        SimpleDateFormat format = new SimpleDateFormat(DATE_PATTERN, Locale.ENGLISH);
        String output="";
        Date docDate= null;
        String headerText = "Неверно заполнено поле даты. Должен быть формат <День.Месяц.Год>, т.е. 31.12.2019";
        try {
            docDate = format.parse(date);
        } catch (ParseException e) {

            Controller.showAlertMessage(headerText, e.toString(), Alert.AlertType.ERROR);
        }
             output = format.format(docDate);
        return output;
    }
}
