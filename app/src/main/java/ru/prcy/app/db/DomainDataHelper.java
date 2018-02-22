package ru.prcy.app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Filter;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.prcy.app.data.DomainSuggestion;

/**
 * Created by dmitry on 26.10.17.
 */

public class DomainDataHelper extends SQLiteOpenHelper {


    private static final int MAX_DOMAIN_HISTORY_ROWS = 200;
    private static final int DOMAIN_HISTORY_SUGGESTIONS_SIZE = 3;
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_DOMAIN_HISTORY =
            "CREATE TABLE " + DomainHistoryContract.DomainHistory.TABLE_NAME + " (" +
                    DomainHistoryContract.DomainHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME + TEXT_TYPE + COMMA_SEP +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_UPDATE_TIMESTAMP + INT_TYPE + COMMA_SEP +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_TIMESTAMP + INT_TYPE + COMMA_SEP +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON + TEXT_TYPE + COMMA_SEP +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_DATA + TEXT_TYPE +
            " )";

    private static final String SQL_CREATE_DOMAIN_FAVOURITES =
            "CREATE TABLE " + DomainFavouritesContract.FavouriteDomain.TABLE_NAME + " (" +
                    DomainHistoryContract.DomainHistory._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME + TEXT_TYPE + COMMA_SEP +
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON + TEXT_TYPE +
                    " )";

//    private static final String SQL_DELETE_DOMAIN_FROM_FAVOURITES = "delete " +
//            "from " + DomainFavouritesContract.FavouriteDomain.TABLE_NAME +
//            " where " + DomainFavouritesContract.FavouriteDomain.COLUMN_NAME_DOMAIN_NAME + "=?" +
//            ")";

    private static final String SQL_DELETE_OLD_DOMAIN_HISTORY = "delete " +
            "from " + DomainHistoryContract.DomainHistory.TABLE_NAME +
            " where " + DomainHistoryContract.DomainHistory._ID + " not in (" +
            "    select " + DomainHistoryContract.DomainHistory._ID +
            "    from " + DomainHistoryContract.DomainHistory.TABLE_NAME +
            "    order by " + DomainHistoryContract.DomainHistory._ID + " desc " +
            "    limit " + MAX_DOMAIN_HISTORY_ROWS +
            ")";

    private static final String SQL_SELECT_LAST_DOMAIN_HISTORY = "select * " +
            "from " + DomainHistoryContract.DomainHistory.TABLE_NAME +
            " where " + DomainHistoryContract.DomainHistory._ID + " in (" +
            "    select " + DomainHistoryContract.DomainHistory._ID +
            "    from " + DomainHistoryContract.DomainHistory.TABLE_NAME +
            "    order by " + DomainHistoryContract.DomainHistory._ID + " desc " +
            "    limit " + DOMAIN_HISTORY_SUGGESTIONS_SIZE +
            ") order by " + DomainHistoryContract.DomainHistory._ID + " desc";

    private static final String SQL_DELETE_DOMAIN_HISTORY =
            "DROP TABLE IF EXISTS " + DomainHistoryContract.DomainHistory.TABLE_NAME;

    private static final String SQL_DELETE_DOMAIN_FAVOURITES =
            "DROP TABLE IF EXISTS " + DomainFavouritesContract.FavouriteDomain.TABLE_NAME;

    private static final String SQL_SELECT_MAX_ID_FROM_DOMAIN_HISTORY =
            "SELECT MAX(" + DomainHistoryContract.DomainHistory._ID + ") from " + DomainHistoryContract.DomainHistory.TABLE_NAME;

    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "domain.db";
    Gson gson;

    public DomainDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.gson = new Gson();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_DOMAIN_HISTORY);
        db.execSQL(SQL_CREATE_DOMAIN_FAVOURITES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_DOMAIN_HISTORY);
        db.execSQL(SQL_DELETE_DOMAIN_FAVOURITES);
        onCreate(db);
    }

    private void deleteAllDomainRows(SQLiteDatabase db, String domain) {
        db.delete(DomainHistoryContract.DomainHistory.TABLE_NAME, DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME + "=?", new String[] {domain});
    }

    private void deleteOldDomainHistoryRows(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_OLD_DOMAIN_HISTORY);
    }

    public long addDomainToHistory(DomainData domainData) {
        SQLiteDatabase db = this.getWritableDatabase();

        deleteAllDomainRows(db, domainData.getDomain());
        deleteOldDomainHistoryRows(db);

        ContentValues values = new ContentValues();
        values.put(DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME, domainData.getDomain());
        values.put(DomainHistoryContract.DomainHistory.COLUMN_NAME_UPDATE_TIMESTAMP, domainData.getUpdated().getTime() / 1000);
        values.put(DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_TIMESTAMP, domainData.getAnalized().getTime() / 1000);
        values.put(DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON, domainData.getFavicon());
        values.put(DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_DATA, domainData.getData().toString());

        long rowId = db.insert(DomainHistoryContract.DomainHistory.TABLE_NAME, null, values);
        db.close();
        return rowId;
    }



    public long addDomainToFavourites(FavouriteDomainData domainData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(DomainFavouritesContract.FavouriteDomain.COLUMN_NAME_DOMAIN_NAME, domainData.getDomain());
        values.put(DomainFavouritesContract.FavouriteDomain.COLUMN_NAME_FAVICON, domainData.getFavicon());

        long rowId = db.insert(DomainFavouritesContract.FavouriteDomain.TABLE_NAME, null, values);
        db.close();
        return rowId;
    }

    public void deleteDomainFromFavourites(String domain) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(DomainFavouritesContract.FavouriteDomain.TABLE_NAME, DomainFavouritesContract.FavouriteDomain.COLUMN_NAME_DOMAIN_NAME + "=?",
                new String[] {domain});
        db.close();
    }

    public boolean isDomainFavourite(String domain) {
        SQLiteDatabase db = this.getReadableDatabase();
        long count = DatabaseUtils.queryNumEntries(db, DomainFavouritesContract.FavouriteDomain.TABLE_NAME,
                DomainFavouritesContract.FavouriteDomain.COLUMN_NAME_DOMAIN_NAME + "=?", new String[] {domain});
        db.close();
        return count > 0;

    }

    public List<FavouriteDomainData> getFavourites() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<FavouriteDomainData> result = new ArrayList<>();
        Cursor cursor = db.query(DomainFavouritesContract.FavouriteDomain.TABLE_NAME, new String[] {
                        DomainHistoryContract.DomainHistory._ID,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON
                }, null, null, null, null, null
        );

        if(cursor != null && cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                result.add(getFavouriteDomainDataFromCursor(cursor));
            }

            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }

        return result;
    }

    private long getMaxDomainHistoryId(SQLiteDatabase db) {
        long result = -1;
        Cursor cursor = db.rawQuery(SQL_SELECT_MAX_ID_FROM_DOMAIN_HISTORY, new String[] {});
        if(cursor != null && cursor.moveToFirst())
            result = cursor.getLong(0);
        cursor.close();
        return result;
    }

    public DomainData getLastFromHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        long id = getMaxDomainHistoryId(db);
        DomainData result = getDomainFromHistory(id);
        db.close();
        return result;
    }

    private DomainData getDomainDataFromCursor(Cursor cursor) {

        long updateTime = cursor.getInt(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_UPDATE_TIMESTAMP));
        long analizeTime = cursor.getInt(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_TIMESTAMP));

        JsonObject data = gson.fromJson(cursor.getString(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_DATA)), JsonObject.class);
        DomainData result = new DomainData(
                cursor.getString(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME)),
                new Date(analizeTime * 1000),
                new Date(updateTime * 1000),
                cursor.getString(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON)),
                data
        );

        return result;
    }

    private FavouriteDomainData getFavouriteDomainDataFromCursor(Cursor cursor) {

        FavouriteDomainData result = new FavouriteDomainData(
                cursor.getString(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME)),
                cursor.getString(cursor.getColumnIndex(DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON))
        );

        return result;
    }

    public DomainData getDomainFromHistory(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DomainHistoryContract.DomainHistory.TABLE_NAME, new String[] {
                    DomainHistoryContract.DomainHistory._ID,
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME,
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_UPDATE_TIMESTAMP,
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_TIMESTAMP,
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON,
                    DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_DATA
                }, DomainHistoryContract.DomainHistory._ID + "=?", new String[] {String.valueOf(id)}, null, null, null, null
        );

        if(cursor != null)
            cursor.moveToFirst();

        if(cursor.getCount() == 0)
            return null;

        DomainData result = getDomainDataFromCursor(cursor);

        cursor.close();
        db.close();

        return result;
    }

    public long getDomainIdFromHistory(String domain) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(DomainHistoryContract.DomainHistory.TABLE_NAME, new String[] {
                        DomainHistoryContract.DomainHistory._ID
                }, DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME + "=?", new String[] {domain}, null, null,
                DomainHistoryContract.DomainHistory._ID + " DESC", "1"
        );

        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            long result = cursor.getLong(cursor.getColumnIndex(DomainHistoryContract.DomainHistory._ID));
            cursor.close();
            db.close();
            return result;
        } else {
            cursor.close();
            db.close();
            return -1;
        }
    }

    public List<DomainData> suggestDomainsFromHistory() {
        SQLiteDatabase db = this.getReadableDatabase();

        List<DomainData> result = new ArrayList<>();
        Cursor cursor = db.rawQuery(SQL_SELECT_LAST_DOMAIN_HISTORY, new String[] {});

        if(cursor != null && cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                result.add(getDomainDataFromCursor(cursor));
            }

            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }

        return result;
    }

    public List<DomainData> searchDomainsFromHistory(String query, int limit) {
        SQLiteDatabase db = this.getReadableDatabase();

        List<DomainData> result = new ArrayList<>();
        Cursor cursor = db.query(DomainHistoryContract.DomainHistory.TABLE_NAME, new String[] {
                        DomainHistoryContract.DomainHistory._ID,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_UPDATE_TIMESTAMP,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_TIMESTAMP,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_FAVICON,
                        DomainHistoryContract.DomainHistory.COLUMN_NAME_ANALIZE_DATA
                }, DomainHistoryContract.DomainHistory.COLUMN_NAME_DOMAIN_NAME + " LIKE ?", new String[] {query + "%"}, null, null,
                DomainHistoryContract.DomainHistory._ID + " DESC", "" + limit
        );

        if(cursor != null && cursor.getCount() > 0) {

            while(cursor.moveToNext()) {
                result.add(getDomainDataFromCursor(cursor));
            }

            cursor.close();
            db.close();
        } else {
            cursor.close();
            db.close();
        }

        return result;
    }



    public void search(Context context, final String query, final int limit, final OnFindSuggestionsListener listener) {
        new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<DomainData> domains = searchDomainsFromHistory(query, limit);

                List<DomainSuggestion> suggestions = new ArrayList<>();
                for(DomainData domain: domains) {
                    suggestions.add(new DomainSuggestion(domain));
                }

                FilterResults results = new FilterResults();
                results.count = suggestions.size();
                results.values = suggestions;

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(listener != null)
                    listener.onResults((List<DomainSuggestion>) results.values);
            }

        }.filter(query);
    }

    public List<DomainSuggestion> suggest() {
        List<DomainData> domains = suggestDomainsFromHistory();

        List<DomainSuggestion> suggestions = new ArrayList<>();
        for(DomainData domain: domains) {
            suggestions.add(new DomainSuggestion(domain));
        }


        return suggestions;
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<DomainSuggestion> results);
    }

}
