package ru.prcy.app.data;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeFieldDate extends AnalizeField{

    private static final String DISPLAY_DATE_FORMAT = "yyyy.MM.dd";
    private static final SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_FORMAT);

    public AnalizeFieldDate(String title, Date value) {
        super(title);
        this.value = value;
    }

    public AnalizeFieldDate(Date value) {
        this.value = value;
    }

    public String title;
    public Date value;

    public static AnalizeFieldDate getValueFromAnalize(JsonObject data, String key, String subKey) {

        String value = data.getAsJsonObject(key).get(subKey).getAsString();
        try {
            Date valueDate = Common.parseJSONDate(value);
            return new AnalizeFieldDate(valueDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return new AnalizeFieldDate(null);
        }

    }

    public String getValueString() {
        return displayDateFormat.format(this.value);
    }

}
