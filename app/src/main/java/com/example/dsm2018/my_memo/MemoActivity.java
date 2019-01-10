package com.example.dsm2018.my_memo;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

public class MemoActivity extends AppCompatActivity {
    private EditText mTitleEditText;
    private  EditText mContentsEditText;
    private long mMemold =-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memo);

        mTitleEditText=(EditText) findViewById(R.id.edit_title);
        mContentsEditText=(EditText) findViewById(R.id.edit_contents);

        Intent intent = getIntent();
        if(intent != null){
            mMemold = intent.getLongExtra("id", mMemold);
            String title = intent.getStringExtra("title");
            String contents = intent.getStringExtra("contents");

            mTitleEditText.setText(title);
            mContentsEditText.setText(contents);
        }
    }




    @Override
    public void onBackPressed() {
        //DB에 저장하기
        String title = mTitleEditText.getText().toString();
        String content = mContentsEditText.getText().toString();

        ContentValues contentValues = new ContentValues();
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_TITLE, title);
        contentValues.put(MemoContract.MemoEntry.COLUMN_NAME_CONTENTS, content);

        SQLiteDatabase db = MemoDbHelper.getSinstance(this).getWritableDatabase();
        if(mMemold == -1){
            //새로추가
            long newPowld = db.insert(MemoContract.MemoEntry.TABLE_NAME, null, contentValues);
            if(newPowld == -1){
                Toast.makeText(this, "저장에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "저장했습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }else{
            int count = db.update(MemoContract.MemoEntry.TABLE_NAME, contentValues, MemoContract.MemoEntry._ID + "=" + mMemold, null);
            if(count == 0){
                Toast.makeText(this, "수정에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(this, "메모가 수정되었습니다.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
            }
        }
        super.onBackPressed();
    }
}
