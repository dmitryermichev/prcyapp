package ru.prcy.app.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

/**
 * Created by dmitry on 25.10.17.
 */

public class ApiKey {

    private static final String PREFS_NAME = "api";
    private static final String KEY_PREFS_KEY = "key";

    Context context;
    String key;

    public ApiKey(Context context, String source) {
        this(context);
        this.key = source;
    }

    public ApiKey(Context context) {
        this.context = context;
    }

    private SharedPreferences getPrefs() {
        return context.getApplicationContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public static ApiKey load(Context context) {
        ApiKey newKey = new ApiKey(context);
        newKey.key = newKey.getPrefs().getString(KEY_PREFS_KEY, null);
        return newKey;
    }

    public boolean isEmpty() {
        return (getKey() == null)? true : false;
    }

    public void save() {
        SharedPreferences preferences = getPrefs();
        preferences.edit().putString(KEY_PREFS_KEY, this.getKey()).commit();
    }

    public void clear() {
        this.key = null;
        save();
    }

    /**
     * Метод проверки, может ли быть исходная строка ключом.
     * @param source
     * @return
     */
    public static boolean isStringAKey(String source) {
        if(source.length() == 32)
            return true;
        else
            return false;
    }

    public String getKey() {
        return this.key;
    }
}
