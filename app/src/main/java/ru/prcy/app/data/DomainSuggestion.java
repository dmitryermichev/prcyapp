package ru.prcy.app.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.Date;

import ru.prcy.app.db.DomainData;

/**
 * Created by dmitry on 30.10.17.
 */

public class DomainSuggestion implements SearchSuggestion {

    /*
    private String domain;
    private Date analized;
    private Date updated;
    private String favicon;
    private JsonObject data;
     */

    public static final Creator<DomainSuggestion> CREATOR = new Creator<DomainSuggestion>() {
        @Override
        public DomainSuggestion createFromParcel(Parcel in) {
            return new DomainSuggestion(in);
        }

        @Override
        public DomainSuggestion[] newArray(int size) {
            return new DomainSuggestion[size];
        }
    };


    private DomainData domainData;

    public DomainSuggestion(DomainData domainData) {
        this.domainData = domainData;
    }

    public DomainSuggestion(Parcel source) {
        Gson gson = new Gson();
        this.domainData = new DomainData(
           source.readString(),
           new Date(source.readLong()),
           new Date(source.readLong()), source.readString(), gson.fromJson(source.readString(), JsonObject.class)
        );

        this.domainData = (DomainData) source.readSerializable();
    }

    @Override
    public String getBody() {
        return domainData.getDomain();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Gson gson = new Gson();
        dest.writeString(domainData.getDomain());
        dest.writeLong(domainData.getAnalized().getTime());
        dest.writeLong(domainData.getUpdated().getTime());
        dest.writeString(domainData.getFavicon());
        dest.writeString(gson.toJson(domainData.getData()));
//        dest.writeInt(domainData.);
//        dest.writeSerializable(domainData);
    }

    public DomainData getDomainData() {
            return this.domainData;
    }
}
