package com.example.han.voca;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class VocaActivity extends AppCompatActivity {




    TextView addNote;           // 단어장에 추가하기
    TextView vocaList;          // 단어리스트로 가기
    ListView content;           // 단어의 뜻, 예문등 내용이 들어갈 자리
    TextView voca1;             // 단어
    ListView diction;           // 발음기호
    String name;                // intent로 넘겨받은 "datalist"라는 값을 받기 위한 객체?



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);



        // 단어리스트에서 선택한 단어값을 받아서...
        // Textview에 출력하는.....
        voca1 = (TextView) findViewById(R.id.voca1);
        final Intent intent = getIntent();
        name = intent.getStringExtra("datalist");    //intent로 넘겨받은 "datalist"를 name에 담는다.
        voca1.setText(name);                                //담은 객체를 textView에 출력



        //단어리스트로 이동하는 (인텐트)
        vocaList = (TextView) findViewById(R.id.vocaList);
        vocaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),VocaList.class);
                startActivity(intent);

            }
        });



        //단어장 추가  (구현안됨)
        addNote = (TextView) findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "단어장에 추가되었습니다.",Toast.LENGTH_SHORT).show();

            }
        });



        ////////////////////////////////////////////////////////////////////////////////////////
        //내용(뜻, 예문)을 ListView에 담는다. => content
        // 발음기호를      ListView에 담는다. => diction

        content = (ListView) findViewById(R.id.content);
        diction = (ListView) findViewById(R.id.diction);


        ArrayList<VocalistDTO> list = xmlPasing();      // DTO 파싱을 list객체에...
        ArrayList<VocalistDTO> list2 = xmlPasing2();    // DTO 파싱을 list2객체에...

        String[] data = new String[9];                  // 내용부분 <S1> ~ <S9>     은 9개
        String[] data2 = new String[1];                 // 발음기호부분 <diction>   은 1개



        //이름으로 값을 비교해서 s1~s9을...
        if(list.size()>0&&name!=null) {
            for(int i=0;i<list.size();i++) {
                if(name.equals(list.get(i).getTitle())) {
                    data[0] = list.get(i).getS1();
                    data[1] = list.get(i).getS2();
                    data[2] = list.get(i).getS3();
                    data[3] = list.get(i).getS4();
                    data[4] = list.get(i).getS5();
                    data[5] = list.get(i).getS6();
                    data[6] = list.get(i).getS7();
                    data[7] = list.get(i).getS8();
                    data[8] = list.get(i).getS9();

                }
            }
        }else{
            data[0] = "";
            data[1] = "";
            data[2] = "";
            data[3] = "";
            data[4] = "";
            data[5] = "";
            data[6] = "";
            data[7] = "";
            data[8] = "";
        }

        //이름으로 값을 비교해서 발음기호를....
        if(list2.size()>0&&name!=null) {
            for(int i=0;i<list2.size();i++) {
                if(name.equals(list2.get(i).getTitle())) {
                    data2[0] = list2.get(i).getDiction();

                }
            }

        }else{
            data2[0] = "";

        }


        // s1~s9 (뜻, 예문)을 adapter에 담는다.
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.simple_list_item_single_choice,
                data);
        // diction을 adapter1에 담는다.
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,
                data2);


        content.setAdapter(adapter);        // adapter을 content에
        diction.setAdapter(adapter2);       // adapter1을 diction에




        ////////////////////////////////////////////////////////////////////////////////////////

    }


    //발음부분(파싱)
    private ArrayList<VocalistDTO> xmlPasing2() {

        ArrayList<VocalistDTO> allList2 = new ArrayList<>();

        try{
            InputStream is = getResources().openRawResource(R.raw.korea);


            //  XmlPullParrser

            XmlPullParserFactory factory2 = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory2.newPullParser();
            parser.setInput(new InputStreamReader(is,"UTF-8"));

            int eventType2 = parser.getEventType();

            VocalistDTO stu2 = null;


            //  문서 끝까지 반복
            while (eventType2 != XmlPullParser.END_DOCUMENT)
            {


                switch (eventType2)
                {

                    //시작태그
                    case XmlPullParser.START_TAG:

                        String startTag2 = parser.getName(); //태그 이름



                        if("mean".equals(startTag2))
                        {
                            stu2 = new VocalistDTO();
                        }

                        if("title".equals(startTag2))
                        {
                            stu2.setTitle(parser.nextText());
                        }

                        else if("diction".equals(parser.getName()))
                        {
                            stu2.setDiction(parser.nextText());
                        }


                        break;





                    //끝태그
                    case XmlPullParser.END_TAG:
                        String endTag2 = parser.getName();
                        if("mean".equals(endTag2))
                        {
                            allList2.add(stu2);
                        }

                        break;
                }
                eventType2 = parser.next();  // 다음 라인 읽기
            }
            is.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
        return allList2;



    }

    //내용부분(파싱)
    private ArrayList<VocalistDTO> xmlPasing() {


        ArrayList<VocalistDTO> allList = new ArrayList<>();

        try{
            InputStream is = getResources().openRawResource(R.raw.korea);


            //  XmlPullParrser

            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(new InputStreamReader(is,"UTF-8"));

            int eventType = parser.getEventType();

            VocalistDTO stu = null;


            //  문서 끝까지 반복
            while (eventType != XmlPullParser.END_DOCUMENT)
            {


                switch (eventType)
                {

                    //시작태그
                    case XmlPullParser.START_TAG:

                        String startTag = parser.getName(); //태그 이름



                        if("mean".equals(startTag))
                        {
                            stu = new VocalistDTO();
                        }

                        if("title".equals(startTag))
                        {
                            stu.setTitle(parser.nextText());
                        }

                        else if("s1".equals(parser.getName()))
                        {
                            stu.setS1(parser.nextText());
                        }

                        else if("s2".equals(parser.getName()))
                        {
                            stu.setS2(parser.nextText());
                        }
                        else if("s2".equals(parser.getName()))
                        {
                            stu.setS2(parser.nextText());
                        }
                        else if("s3".equals(parser.getName()))
                        {
                            stu.setS3(parser.nextText());
                        }
                        else if("s4".equals(parser.getName()))
                        {
                            stu.setS4(parser.nextText());
                        }
                        else if("s5".equals(parser.getName()))
                        {
                            stu.setS5(parser.nextText());
                        }
                        else if("s6".equals(parser.getName()))
                        {
                            stu.setS6(parser.nextText());
                        }
                        else if("s7".equals(parser.getName()))
                        {
                            stu.setS7(parser.nextText());
                        }
                        else if("s8".equals(parser.getName()))
                        {
                            stu.setS8(parser.nextText());
                        }
                        else if("s9".equals(parser.getName()))
                        {
                            stu.setS9(parser.nextText());
                        }
                        break;





                    //끝태그
                    case XmlPullParser.END_TAG:
                        String endTag = parser.getName();
                        if("mean".equals(endTag))
                        {
                            allList.add(stu);
                        }

                        break;
                }
                eventType = parser.next();  // 다음 라인 읽기
            }
            is.close();

        }catch (Exception e)
        {
            e.printStackTrace();
        }
     return allList;
    }


}
