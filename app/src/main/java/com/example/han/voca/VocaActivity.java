package com.example.han.voca;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class VocaActivity extends AppCompatActivity {




    TextView addNote;           // 단어장에 추가하기
    TextView vocaList;          // 단어리스트로 가기
    TextView voca1;              // 단어
    RatingBar ratingBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voca);




        // 단어리스트에서 넘겨받은 객체 출력
        TextView voca1 = (TextView) findViewById(R.id.voca1);
        Intent intent = getIntent();

        String name = intent.getStringExtra("datalist");    //intent로 넘겨받은 "datalist"를 name에 담는다.
        voca1.setText(name);                                //담은 객체를 textView에 출력



        //단어리스트로 가기(인텐트)
        vocaList = (TextView) findViewById(R.id.vocaList);
        vocaList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getApplicationContext(),VocaList.class);
                startActivity(intent);

            }
        });




        //중요도
        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Toast.makeText(getApplicationContext(), "설정 값 : "+v, Toast.LENGTH_SHORT);
            }
        });




        //단어장 추가
        addNote = (TextView) findViewById(R.id.addNote);
        addNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "단어장에 추가되었습니다.",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
