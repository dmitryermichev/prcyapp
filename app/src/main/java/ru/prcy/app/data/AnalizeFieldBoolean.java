package ru.prcy.app.data;

import com.google.gson.JsonObject;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeFieldBoolean extends AnalizeField{


    public AnalizeFieldBoolean(String title, boolean value) {
        super(title);
        this.value = value;
    }


    public AnalizeFieldBoolean(boolean value) {
        this.value = value;
    }

    public String title;
    public boolean value;

    public static AnalizeFieldBoolean getValueFromAnalize(JsonObject data, String key, String subKey) {
        boolean value = false;
        if(data != null && data.getAsJsonObject(key) != null && data.getAsJsonObject(key).get(subKey) != null)
            value = data.getAsJsonObject(key).get(subKey).getAsBoolean();

        return new AnalizeFieldBoolean(value);

    }

}
