package ru.prcy.app.data;

import com.google.gson.JsonObject;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeFieldString extends AnalizeField{

    public AnalizeFieldString(String title, String value) {
        super(title);
        this.value = value;
    }

    public AnalizeFieldString(String value) {
        this.value = value;
    }

    public String title;
    public String value;

    public static AnalizeFieldString getValueFromAnalize(JsonObject data, String key, String subKey) {
        return new AnalizeFieldString(data.getAsJsonObject(key).get(subKey).getAsString());
    }

}
