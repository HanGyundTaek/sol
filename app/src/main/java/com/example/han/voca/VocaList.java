package com.example.han.voca;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by Han on 2016-09-05.
 */
public class VocaList extends AppCompatActivity {



    private ListView list;
    dbHelper helper;
    SQLiteDatabase db;
    ArrayList<VocaDTO> dataList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocalist);



        list = (ListView) findViewById(R.id.list);



        // db를  Listview에 출력하는 코드
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from contacts", null);

        //데이터 얻기??
        while(cursor.moveToNext()){
            VocaDTO dto = new VocaDTO();
            dto.setVoca(cursor.getString(1));   // 0번컬럼은 시퀸스넘버, 1번 컬럼은 name, 2번 컬럼은 date
            dto.setDate(cursor.getString(2));   // 0번컬럼은 시퀸스넘버, 1번 컬럼은 name, 2번 컬럼은 date
            Log.i("test", "onCreate: "+dto.getVoca());
            dataList.add(dto);                  // dto를 'datalist'에 추가
        }
        startManagingCursor(cursor);



        // 컬럼 name과 date를 ...... , 자료를 adapter에 담음.
        String[] from = {"name", "date"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this    , android.R.layout.simple_list_item_2, cursor, from, to);

        //담았던 adapter를 list에 출력
        list.setAdapter(adapter);


        //리스트를 클릭하면 발생하는 이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                                    //부모 어뎁터,     클릭된 뷰,     뷰의 포지션번호,   뷰의 아이디.

                //   [나중에 지울것] db에 값을 담은 Toast를 출력
                Toast.makeText(VocaList.this, dataList.get(position).getVoca(), Toast.LENGTH_SHORT).show();




                //단어를 누르면   '리스트 ->  뜻'   이동하는 인텐트
                Intent intent = new Intent(getApplicationContext(),VocaActivity.class);
                intent.putExtra("datalist",dataList.get(position).getVoca());
                startActivity(intent);

            }
        });
 }


}





// 임의로 만든 db
// 나중에 지워야함.

class dbHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "mydb_3.db";
    private static final int DATABASE_VERSION = 2;

    public dbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE contacts ( _id integer primary key autoincrement, "
                + "name VOCA, date DATE);");

        for (int i = 0; i<10; i++){
            db.execSQL("insert into contacts values(null, "+"'단어"+i
                    +"'"+", '날짜"+i+"');");
        }

    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}

