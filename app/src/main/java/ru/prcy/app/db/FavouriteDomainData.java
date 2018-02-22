package ru.prcy.app.db;

import com.google.gson.JsonObject;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by dmitry on 26.10.17.
 */

public class FavouriteDomainData implements Serializable{

    private String domain;
    private String favicon;


    public FavouriteDomainData(String domain, String favicon) {
        this.domain = domain;
        this.favicon = favicon;
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }
}
