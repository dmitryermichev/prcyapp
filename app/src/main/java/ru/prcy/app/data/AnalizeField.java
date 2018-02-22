package ru.prcy.app.data;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeField {

    protected static final String CURRENT_DATE_DAY_FORMAT = "yyyyMMdd";
    protected static final SimpleDateFormat dayFormat = new SimpleDateFormat(CURRENT_DATE_DAY_FORMAT);

    protected  static DecimalFormat intFormatter;

    public String title;

    public AnalizeField(String title) {
        this.title = title;
    }

    public AnalizeField() {

    }

    protected DecimalFormat getIntFormatter() {
        if(intFormatter != null)
            return intFormatter;

        intFormatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
        DecimalFormatSymbols symbols = intFormatter.getDecimalFormatSymbols();
        symbols.setGroupingSeparator(' ');
        intFormatter.setDecimalFormatSymbols(symbols);
        return intFormatter;
    }

}
