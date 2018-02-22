package ru.prcy.app.db;

import android.provider.BaseColumns;

/**
 * Created by dmitry on 26.10.17.
 */

public final class DomainFavouritesContract {

    public DomainFavouritesContract() {}

    public static abstract class FavouriteDomain implements BaseColumns {
        public static final String TABLE_NAME = "domain_favourites";
        public static final String COLUMN_NAME_DOMAIN_NAME = "domain";
        public static final String COLUMN_NAME_FAVICON = "favicon";
    }
}
