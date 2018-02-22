package ru.prcy.app.db;

import android.provider.BaseColumns;

/**
 * Created by dmitry on 26.10.17.
 */

public final class DomainHistoryContract {

    public DomainHistoryContract() {}

    public static abstract class DomainHistory implements BaseColumns {
        public static final String TABLE_NAME = "domain_history";
        public static final String COLUMN_NAME_DOMAIN_NAME = "domain";
        public static final String COLUMN_NAME_UPDATE_TIMESTAMP = "update_timestamp";
        public static final String COLUMN_NAME_ANALIZE_TIMESTAMP = "analize_timestamp";
        public static final String COLUMN_NAME_FAVICON = "favicon";
        public static final String COLUMN_NAME_ANALIZE_DATA = "analize_data";
    }
}
