package ru.prcy.app.api;

import com.google.gson.JsonObject;

import java.util.Date;

/**
 * Created by dmitry on 26.10.17.
 */

public class AnalizeResult {

    private AnalizeResult(Exception e) {
        this.result = 0;
        this.exception = e;
    }

    private AnalizeResult(Date updatedAt, JsonObject object) {
        this.result = 1;
        this.updatedAt = updatedAt;
        this.body = object;
    }

    public boolean isSucceed() {
        return (result == 1) ? true : false;
    }

    public JsonObject getBody() {
        return this.body;
    }

    public Exception getException() {
        return this.exception;
    }

    public Date getUpdatedAt() {return this.updatedAt;}

    private int result;
    private Date updatedAt;
    private JsonObject body;
    private Exception exception;

    public static AnalizeResult error(Exception e) {
        return new AnalizeResult(e);
    }

    public static AnalizeResult success(Date updatedAt, JsonObject json) {
        return new AnalizeResult(updatedAt, json);
    }
}
