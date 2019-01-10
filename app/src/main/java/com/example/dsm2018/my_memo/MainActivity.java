package com.example.dsm2018.my_memo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_INSERT = 1000;
    MemoAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listView = (ListView) findViewById(R.id.memo_list);
        adapter = new MemoAdapter(this,getMemoCursor());
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, MemoActivity.class);
                Cursor cursor = (Cursor) adapter.getItem(position);

                //커서에서 제목과 내용을 가져옴
                String title = cursor.getString(
                        cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE));
                String contents = cursor.getString(
                        cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_CONTENTS));

                //인텐트에 id와 함께 저장
                intent.putExtra("id", id);
                intent.putExtra("title", title);
                intent.putExtra("contents", contents);

                //MEMOActivity시작
                startActivityForResult(intent,REQUEST_CODE_INSERT);
            }
        });

       listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
           @Override
           public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               final long deleteld = id;

               AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
               builder.setTitle("메모 삭제");
               builder.setMessage("메모를 삭제하시겠습니까?");
               builder.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialogInterface, int i) {
                       SQLiteDatabase db = MemoDbHelper.getSinstance(MainActivity.this).getWritableDatabase();
                       int deleteCount = db.delete(MemoContract.MemoEntry.TABLE_NAME,
                               MemoContract.MemoEntry._ID + "=" + deleteld, null);
                       if (deleteCount == 0) {
                           Toast.makeText(MainActivity.this, "삭제에 문제가 발생하였습니다.", Toast.LENGTH_SHORT).show();
                       } else {
                           adapter.swapCursor(getMemoCursor());
                           Toast.makeText(MainActivity.this, "메모가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
               builder.setNegativeButton("취소", null);
               builder.show();
               return true;
           }
       });

    }

    public void MEMOACTIVITY(View view){
        Intent intent = new Intent(this,MemoActivity.class);
        startActivityForResult(intent, REQUEST_CODE_INSERT);
    }

    private  Cursor getMemoCursor(){
        MemoDbHelper dbHelper = MemoDbHelper.getSinstance(this);

        //테이블명, 결과를 얻을 컬럼들(배열로정의, null이면 전부 가져옴)
        //where정의 컬럼, where절의 값, group by 절, having 절, orderby절
        Cursor cursor = dbHelper.getReadableDatabase()
                .query(MemoContract.MemoEntry.TABLE_NAME,null,null,null,null,null,
                        MemoContract.MemoEntry._ID+" DESC");
        return  cursor;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_INSERT && resultCode == RESULT_OK){
            //swapCursor 메서드에 새로 갱신된 커서를 전달
            adapter.swapCursor(getMemoCursor());
        }
    }

    private static class MemoAdapter extends CursorAdapter {
        public MemoAdapter(Context context, Cursor c) {super(context, c, false);}

        //리스트뷰의 각 아이템에 해당할 레이아웃
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup viewGroup){
            return LayoutInflater.from(context)
                    .inflate(android.R.layout.simple_list_item_1, viewGroup, false);
        }

        //Cursor객체의 컬럼 값을 가져와서 설정
        @Override
        public void bindView(View view, Context context, Cursor cursor){
            TextView titleText = view.findViewById(android.R.id.text1);
            titleText.setText(cursor.getString(
                    cursor.getColumnIndexOrThrow(MemoContract.MemoEntry.COLUMN_NAME_TITLE)));
        }
    }
}
