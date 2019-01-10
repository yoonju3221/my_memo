package com.example.dsm2018.my_memo;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MemoDbHelper extends SQLiteOpenHelper {

    private static MemoDbHelper sinstance;

    //db의 버전으로 1부터 시작하고 스카마가 변경될 때 숫자를 올린다.
    private static final int DB_VERSION = 1;

    //DB파일명
    private static final String DB_NAME = "Memo.db";

    //테이블 생성 sql문
    private static final String SQL_CREATE_ENTRIES =
            String .format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT)",
            MemoContract.MemoEntry.TABLE_NAME,
                    MemoContract.MemoEntry._ID,
                    MemoContract.MemoEntry.COLUMN_NAME_TITLE,
                    MemoContract.MemoEntry.COLUMN_NAME_CONTENTS);

    //테이블 삭제 SQL문
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MemoContract.MemoEntry.TABLE_NAME;
    public static synchronized MemoDbHelper getSinstance(Context context){
        //액티비티의 context가 메모리 릭을 발생할 수 있으므로 어플리케이션 context를 사용하는 것이 좋다
        if(sinstance == null){
            sinstance = new MemoDbHelper(context.getApplicationContext());
        }
        return sinstance;
    }
    private MemoDbHelper(Context context){
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_CREATE_ENTRIES);
        onCreate(db);
    }
}
