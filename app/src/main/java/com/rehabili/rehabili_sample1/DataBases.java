package com.rehabili.rehabili_sample1;

import android.provider.BaseColumns;

public final class DataBases {
    public static final class CreateDB implements BaseColumns {
        public static final String DATETIME = "datetime";
        public static final String TYPE = "type";
        public static final String LEVEL = "level";
        public static final String TIMES = "times";
        public static final String _TABLENAME0 = "History";
        public static final String _CREATE0 = "CREATE TABLE if not exists "+_TABLENAME0+"("
                +_ID+" integer PRIMARY KEY autoincrement, "
                +DATETIME+" timestamp not null , "
                +TYPE+" text not null , "
                +LEVEL+" integer not null , "
                +TIMES+" integer not null );";
    }
}
