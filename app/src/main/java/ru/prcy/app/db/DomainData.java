package ru.prcy.app.db;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dmitry on 26.10.17.
 */

public class DomainData implements Serializable {

    private String domain;
    private Date analized;
    private Date updated;
    private String favicon;
    private JsonObject data;


    public DomainData(String domain, Date analized, Date updated, String favicon, JsonObject data) {
        this.domain = domain;
        this.analized = analized;
        this.updated = updated;
        this.favicon = favicon;
        this.data = data;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Date getUpdated() {
        return updated;
    }

    public void setUpdated(Date updated) {
        this.updated = updated;
    }

    public JsonObject getData() {
        return data;
    }

    public void setData(JsonObject data) {
        this.data = data;
    }

    public Date getAnalized() {
        return analized;
    }

    public void setAnalized(Date analized) {
        this.analized = analized;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
}
