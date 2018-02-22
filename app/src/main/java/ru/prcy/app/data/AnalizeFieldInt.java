package ru.prcy.app.data;

import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeFieldInt extends AnalizeField{

    public AnalizeFieldInt(String title, long value, long delta) {
        super(title);
        this.value = value;
        this.delta = delta;
    }

    public AnalizeFieldInt(String title, long value) {
        super(title);
        this.value = value;
        this.delta = 0;
    }

    public AnalizeFieldInt(long value, long delta) {
        this.value = value;
        this.delta = delta;
    }

    public String title;
    private long value;
    private long delta;

    public static AnalizeFieldInt getValueFromAnalize(JsonObject data, String key, String subKey) {

        long value = data.getAsJsonObject(key).get(subKey).getAsLong();
        long delta = 0;
        String yesterday = dayFormat.format(new Date(new Date().getTime() - 24 * 3600 * 1000));

        if(
                data.has(key) &&
                        data.getAsJsonObject(key).has(subKey + "History") &&
                        data.getAsJsonObject(key).getAsJsonObject(subKey + "History").getAsJsonObject("days").has(yesterday)
                ) {
            long yesterdayValue = data.getAsJsonObject(key).getAsJsonObject(subKey + "History").getAsJsonObject("days").get(yesterday).getAsLong();
            delta = value - yesterdayValue;
        }

        return new AnalizeFieldInt(value, delta);

    }

    public long getValue() {
        return this.value;
    }

    public String getDeltaString() {
        if(this.delta < 0) {
            return "" + delta;
        } else if(this.delta == 0) {
            return "";
        } else {
            return "+" + this.delta;
        }
    }

    public long getDelta() {
        return this.delta;
    }

    public String getValueString() {
        return getIntFormatter().format(this.value);
    }


}
