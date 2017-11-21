package express.convert.convertexpressmobileapp;

import android.provider.BaseColumns;

public final class HistoryDbContext {
    private HistoryDbContext() {}

    public static class HistoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "history";
        public static final String COLUMN_NAME_QUERY = "query";
    }
}

