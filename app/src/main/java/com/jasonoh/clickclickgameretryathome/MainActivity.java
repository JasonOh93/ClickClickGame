package com.jasonoh.clickclickgameretryathome;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import android.os.Bundle;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends AppCompatActivity {

    TextView tv, tv02;
    ImageView iv_start;
    ImageView[] ivs = new ImageView[30];
    LinearLayout ll;

    AdView adView;

    int count = 0, stage = 1;

    boolean isRun = true;
    //int cnt_millSecond = 0, cnt_second = 0, cnt_min = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //참조 변수에 해당하는 값 아이디 찾아오기
        tv = findViewById(R.id.tv);
        tv02 = findViewById(R.id.tv02);
        iv_start = findViewById(R.id.iv_start);
        ll = findViewById(R.id.ll);

        adView = findViewById(R.id.adv);

        //광고 요청 객체 생성
        adView.loadAd( new AdRequest.Builder().build() );

        for(int i = 0; i < ivs.length; i++) {
            ivs[i] = findViewById(R.id.iv01 + i);
        } // for 문

        //start를 누르면 실행
        iv_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_start.setImageResource(R.drawable.ing);
                tv.setText("작은 숫자에서 큰 숫자 순으로 누르세요");
                ll.setBackgroundResource(R.drawable.bg_02);
                initial();
                iv_start.setClickable(false);
                isRun = true;

                //Thread 사용하여 초시계 넣기
                new Thread(){
                    @Override
                    public void run() {

//                        cnt_millSecond = 0;
//                        cnt_second = 0;
//                        cnt_min = 0;

                        //인터넷 실험용
                        final long startTime = System.currentTimeMillis();
                        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat( "mm : ss : SSS" );

                        while ( isRun ) {
//                            cnt_millSecond++;
//                            if( (cnt_millSecond == 100) && (cnt_second < 60) ) { cnt_millSecond = 0; cnt_second++; }
//                            if( (cnt_second == 60) && (cnt_min < 60) ) { cnt_second = 0; cnt_min++; }

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    //인터넷 실험용
                                    long end = System.currentTimeMillis();
                                    tv.setText( simpleDateFormat.format( end - startTime ).substring(0, 12) );
                                    if(stage > 2) tv.setText("축하합니다!! 모두 클리어 하셨습니다@@");

//                                    tv.setText( cnt_min + " : " + cnt_second + " : " + cnt_millSecond );
                                }
                            });
                            SystemClock.sleep(10);

//                            try { //이게 아닌거 같음
//                                if(cnt_millSecond < 100) Thread.sleep(10);
//                                if( (cnt_millSecond == 100) && (cnt_second < 60) ) Thread.sleep(1000);
//                                if( (cnt_second == 60) && (cnt_min < 60) ) Thread.sleep(10000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }

                        }//while

                    }//run method
                }.start();

            }// 익명클래스 내부 클래스
        }); // iv_start의 setOnClickListener Anonymous class

    }

    //버튼 객체 생성
    public void ivClick(View v) {
        ImageView ivtemp = (ImageView)v;

        if( (Integer.parseInt( ivtemp.getTag().toString() ) == count) ) {
            ivtemp.setVisibility(View.INVISIBLE);

            if(stage == 1) { count++; tv02.setText( "찾으셔야 하는 수는    " + (count + 1) + " 입니다." ); }
            if(stage == 2) { count--; tv02.setText( "찾으셔야 하는 수는   " + (count + 1) + " 입니다." ); }
        } //if문

        if( (stage == 1) && (count > (ivs.length - 1)) ) {
            count = 29;
            stage++;
            initial();
        } //if
        else if( (stage == 2) && (count < 0) ) {
            count = 0;
            stage++;
            initial();
        }

    }//ivClick method

    //초기 설정 값 설정
    void initial() {

        if(stage == 1) tv02.setText( "찾으셔야 하는 숫자는    " + (count + 1) + " 입니다." );
        else if(stage == 2) tv02.setText( "찾으셔야 하는 숫자는   " + (count + 1) + " 입니다." );

        ArrayList<Integer> arrayList = new ArrayList<>();

        for(int i = 0; i < ivs.length; i++) {
            arrayList.add( i );
        }//for arrayList

        Collections.shuffle( arrayList );

        for(int i = 0; i < ivs.length; i++) {
            if( (stage > 0) && (stage < 3) ) {
                if(stage == 1) ivs[i].setImageResource( R.drawable.num_01 + arrayList.get(i) );
                else if(stage == 2) ivs[i].setImageResource( R.drawable.num_01 + arrayList.get(i) );

                ivs[i].setVisibility(View.VISIBLE);
                ivs[i].setTag( arrayList.get(i) );
            }//if
            else ivs[i].setVisibility(View.INVISIBLE);
        }//for ivs[]

        //for문 안에서 tv나 ll이 계속 반복해서 이미지를 넣어 주는 것이 별로 좋지 않음 그래서 여기서 한번만 넣어주기
        if(stage == 2) {
            ll.setBackgroundResource(R.drawable.bg_03);
            //tv.setText("큰 숫자에서 작은 숫자 순으로 누르세요");
        } // stage == 2 인 if문
        else if(stage > 2) {
            isRun = false;
            ll.setBackgroundResource(R.drawable.bg_04);

            stage = 1;
            iv_start.setImageResource(R.drawable.start);
            iv_start.setClickable(true);
            tv02.setText( tv.getText().toString() );
            tv.setText("축하합니다!! 모두 클리어 하셨습니다@@");

        } //stage > 2 else if문 , 즉 게임이 끝난 것
    }//initial method...
}
