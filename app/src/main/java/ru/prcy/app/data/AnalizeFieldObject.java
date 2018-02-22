package ru.prcy.app.data;

import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Created by dmitry on 27.10.17.
 */

public class AnalizeFieldObject extends AnalizeField{

    public AnalizeFieldObject(String title, JsonObject object) {
        super(title);
        this.object = object;
    }

    public AnalizeFieldObject(JsonObject object) {
        this.object = object;
    }


    public String title;
    public JsonObject object;

    public static AnalizeFieldObject getValueFromAnalize(JsonObject data, String key, String subKey) {

        return new AnalizeFieldObject(data.getAsJsonObject(key).getAsJsonObject(subKey));

    }


}
