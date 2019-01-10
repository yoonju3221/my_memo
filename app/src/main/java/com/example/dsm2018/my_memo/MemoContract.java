package com.example.dsm2018.my_memo;


import android.provider.BaseColumns;

public final class MemoContract {

    //인스턴트화 금지
    private MemoContract(){}

    //테이블의 정보를 내부 클래스로 정의
    public static class MemoEntry implements BaseColumns {
        public static final String TABLE_NAME = "memo";
        public static  final String COLUMN_NAME_TITLE = "title";
        public static  final String COLUMN_NAME_CONTENTS = "contents";
    }
}

