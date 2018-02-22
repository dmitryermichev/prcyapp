package ru.prcy.app.data;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by dmitry on 01.11.17.
 */

public class Common {

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String source) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static Date parseJSONDate(String input ) throws java.text.ParseException {

        //NOTE: SimpleDateFormat uses GMT[-+]hh:mm for the TZ which breaks
        //things a bit.  Before we go on we have to repair this.
        SimpleDateFormat df = new SimpleDateFormat( "yyyy-MM-dd'T'HH:mm:ssz" );

        //this is zero time so we need to add that TZ indicator for
        if ( input.endsWith( "Z" ) ) {
            input = input.substring( 0, input.length() - 1) + "GMT-00:00";
        } else {
            int inset = 6;

            String s0 = input.substring( 0, input.length() - inset );
            String s1 = input.substring( input.length() - inset, input.length() );

            input = s0 + "GMT" + s1;
        }

        return df.parse( input );

    }

    public static String bytesToString(int bytes) {
        if(bytes < 1000) {
            return bytes + "Байт";
        } else if (bytes < 1000000) {
            return bytes / 1024 + "Кб";
        } else {
            return new DecimalFormat("#.##").format((float) bytes / 1024 / 1024) + " Мб";
        }
    }

    public static String kBytesToString(long kbytes) {
        if(kbytes < 1000) {
            return kbytes + "Кб";
        }  else {
            return new DecimalFormat("#.##").format((float) kbytes / 1024 ) + " Мб";
        }
    }

    public static String formatFloat(float value) {
        return new DecimalFormat("#.##").format(value);
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd.MM.yyyy");
        return sdf.format(date);
    }
}
