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



    private ListView list;                                  // 리스트뷰(단어리스트가 들어갈...)
    dbHelper helper;
    SQLiteDatabase db;
    ArrayList<VocaDTO> dataList = new ArrayList<>();        // ArrayList (단어리스트를 담기 위해...)



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vocalist);



        // db에 있는 단어들을 'select ' 문을 통해 'ListView'에 나열
        list = (ListView) findViewById(R.id.list);
        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from contacts", null);



        // DTO객체에 값을 담기?
        while(cursor.moveToNext()){
            VocaDTO dto = new VocaDTO();        // DTO객체 생성
            dto.setVoca(cursor.getString(1));   // 1번 컬럼 'name'을 DTO에 담기
            dto.setDate(cursor.getString(2));   // 2번 컬럼 'date'를 DTO에 담기
                                                // 0번컬럼은 시퀸스넘버, 1번 컬럼은 name, 2번 컬럼은 date
            dataList.add(dto);                  // dto를 datalist에... ( datalist는 ArrayList<VocaDTO>의 객체)
        }
        startManagingCursor(cursor);



        // 컬럼 NAME(단어)과 DATE(날짜)를  adapter에 담음.
        String[] from = {"name", "date"};
        int[] to = {android.R.id.text1, android.R.id.text2};
        SimpleCursorAdapter adapter =
                new SimpleCursorAdapter(this, android.R.layout.simple_list_item_2,
                                        cursor, from, to);

        //담았던 adapter를 listview에 출력
        list.setAdapter(adapter);


        //리스트를 클릭하면 발생하는 이벤트
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position,    long l) {
                                    //  'AdapterView<?> adapterView'  부모 어뎁터
                                    //  'View view'                   클릭된 뷰
                                    //  'int position'                뷰의 포지션번호
                                    //  'long l'                      뷰의 아이디.



                //  db에 값을 담은 Toast를 출력 (지워도 된다.)
                Toast.makeText(VocaList.this, dataList.get(position).getVoca()+"가 선택되었습니다.", Toast.LENGTH_SHORT).show();


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


    public static final String DATABASE_NAME = "mydb_4.db";
    public static final int DATABASE_VERSION = 2;

    public dbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE contacts ( _id integer primary key autoincrement, "
                + "name VOCA, date DATE);");

        db.execSQL("insert into contacts values(null,'time','날짜1');");
        db.execSQL("insert into contacts values(null,'picture','날짜2');");
        db.execSQL("insert into contacts values(null,'beautiful','날짜3');");
        db.execSQL("insert into contacts values(null,'interest','날짜4');");
        db.execSQL("insert into contacts values(null,'give','날짜5');");
        db.execSQL("insert into contacts values(null,'history','날짜6');");
        db.execSQL("insert into contacts values(null,'symbol','날짜7');");

    }

    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS contacts");
        onCreate(db);
    }
}

