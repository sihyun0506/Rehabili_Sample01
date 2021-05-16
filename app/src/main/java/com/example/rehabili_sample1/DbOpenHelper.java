package com.example.rehabili_sample1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.rehabili_sample1.ui.history.HistoryFragment;

public class DbOpenHelper {

    private static final String DATABASE_NAME = "InnerDatabase(SQLite).db";
    private static final int DATABASE_VERSION = 1;
    public static SQLiteDatabase mDB;
    private DatabaseHelper mDBHelper;
    private Context mCtx;
    private HistoryFragment mNFrag;

    private class DatabaseHelper extends SQLiteOpenHelper {
        // 생성자
        public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        // db의 테이블을 생성, 테이블 구성 시 다른 테이블 명칭을 추가하여 작성하면 하나의 db에서 여러 테이블도 생성 가능
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DataBases.CreateDB._CREATE0);
        }

        // 버전 업그레이드 시 사용, 이전 버전을 지우고 새 버전을 생성
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DataBases.CreateDB._TABLENAME0);
            onCreate(db);
        }
    }

    public DbOpenHelper(Context context) {
        this.mCtx = context;
    }

    // 해당 db를 사용할 수 있도록 해줌
    // getWritableDatabase()는 db를 읽고 쓸 수 있게 해줌
    public DbOpenHelper open() throws SQLException {
        mDBHelper = new DatabaseHelper(mCtx, DATABASE_NAME, null, DATABASE_VERSION);
        mDB = mDBHelper.getWritableDatabase();
        return this;
    }

    public void create() {
        mDBHelper.onCreate(mDB);
    }

    // 해당 db를 닫음. 사용 중에는 매번 열고 닫지 않아도 되지만, 모두 사용한 후에는 가급적이면 닫는게 좋음
    public void close() {
        mDB.close();
    }

    // 데이터 삽입
    public long insertColumn(String datetime, String type, String level, int times) {
        ContentValues values = new ContentValues();
        values.put(DataBases.CreateDB.DATETIME, datetime);
        values.put(DataBases.CreateDB.TYPE, type);
        values.put(DataBases.CreateDB.LEVEL, level);
        values.put(DataBases.CreateDB.TIMES, times);
        return mDB.insert(DataBases.CreateDB._TABLENAME0, null, values);
    }

    // 데이터 정렬, 특정 Column을 기준으로 오름차순 정렬
    public Cursor sortColumnUp(String sort) {
        Cursor c = mDB.rawQuery("SELECT * FROM History ORDER BY " + sort + " ASC;", null);
        return c;
    }
    // 데이터 정렬, 특정 Column을 기준으로 내림차순 정렬
    public Cursor sortColumnDown(String sort) {
        Cursor c = mDB.rawQuery("SELECT * FROM History ORDER BY " + sort + " DESC;", null);
        return c;
    }

    // Delete All
    public void deleteAllColumns() {
        mDB.delete(DataBases.CreateDB._TABLENAME0, null, null);
    }

    // Delete Column
    public boolean deleteColumn(long id) {
        return mDB.delete(DataBases.CreateDB._TABLENAME0, "_id=" + id, null) > 0;
    }
}

