package com.example.rehabili_sample1.ui.arm.grip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rehabili_sample1.DbOpenHelper;
import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.Finish;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowCounting;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GripCounting extends AppCompatActivity {

    Vibrator mVib;
    String type;
    private String goalNumber = "20";
    private int count = 0;
    private TextView showMessages;
    private ImageView showImages;
    private TextView showCountNumber;
    boolean isThread = false;
    Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grip_counting);

        //Set에서 받아온 값으로 type과 level과 goalNumber를 설정
        Intent intent = getIntent();
        type = intent.getStringExtra("type");

        //진동기능 발생기의 핸들을 구해서 멤버변수에 저장
        mVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        showMessages = findViewById(R.id.textOut);
        showImages = findViewById(R.id.imageView);
        showCountNumber = findViewById(R.id.showCountNumber);

        showImages.setImageResource(R.drawable.hand_loose);

        //-----------스레드이용하여 카운트----------
        isThread = true;
        thread = new Thread() {
            public void run() {
                try {
                    for (count = 0; count < 20; count++) {
                        handler.sendEmptyMessage(1);
                        mVib.vibrate(300); // 진동
                        sleep(5000);
                        if (count>20) {
                            return;
                        }
                        handler.sendEmptyMessage(0);
                        mVib.vibrate(300); // 진동
                        sleep(5000);
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (count == 20) {
                    // DB에 운동기록 입력
                    insertDB(type, "Lv  -", 20);

                    // 다음 액티비티 ElbowFinish 로 이동 (type과 goal 전송)
                    Intent intent = new Intent(GripCounting.this, Finish.class);
                    intent.putExtra("type", type);
                    intent.putExtra("goalNumber", goalNumber);
                    startActivity(intent);
                }
                finish();

            }
        };
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                showMessages.setText(R.string.griphard);
                showImages.setImageResource(R.drawable.hand_tight);
                showCountNumber.setText(String.valueOf(count));
            } else if (msg.what == 1) {
                showMessages.setText(R.string.gripsoft);
                showImages.setImageResource(R.drawable.hand_loose);
                showCountNumber.setText(String.valueOf(count));
            }
        }
    };

    // 뒤로가기 방지
    @Override
    public void onBackPressed() {
        count = 200;
        super.onBackPressed();
    }

    // 현재 시간 생성
    public String genDateTime() {
        long mNow = System.currentTimeMillis();
        Date mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return mFormat.format(mDate);
    }

    // db에 값을 넣어줌
    public boolean insertDB(String type, String level, int goal) {
        boolean flag = true;
        DbOpenHelper mDbOpenHelper = new DbOpenHelper(this);
        mDbOpenHelper.open();
        mDbOpenHelper.create();

        String dateTime = genDateTime();
        mDbOpenHelper.open();
        // DB정렬을 위해 문자열 변환 후 저장
        type = getString(R.string.grip)+"              ";
        mDbOpenHelper.insertColumn(dateTime, type, level, goal);

        return flag;
    }

}
