package com.example.rehabili_sample1.ui.arm.elbow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.speech.tts.TextToSpeech;
import android.widget.TextView;

import com.example.rehabili_sample1.DbOpenHelper;
import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.Finish;
import com.example.rehabili_sample1.ui.TTSReader;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.atan;
import static android.speech.tts.TextToSpeech.ERROR;

public class ElbowCounting extends AppCompatActivity implements SensorEventListener {

    //모든종류의 센서가 제어가능한 센서관리자
    SensorManager mSensorMgr = null;
    //진동
    Vibrator mVib;
    int count = 0;
    private double Gx, Gy, Gz;

    // Set에서 받아올 값 1.
    public String goalNumber;

    private int goal;
    private TextView showGoalNumber;
    private TextView showCountNumber;
    private TextView showMessages;

    // Set에서 받아올 값 2.
    public String level;

    // Set에서 받아올 값 3.
    public String type;

    private double maxArk = 90;
    private double minArk = 00;
    boolean isThread = false;
    Thread thread;

    // 음성출력
    int wrongAngleCount = 0;
    private TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elbow_counting);

        // tts
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //사용할 언어를 설정
                    Locale systemLocale = getResources().getConfiguration().locale;
                    int result = tts.setLanguage(systemLocale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    } else {
                        //음성 톤
                        tts.setPitch(1);
                        //읽는 속도
                        tts.setSpeechRate(1);

                    }
                }
            }
        });

        showCountNumber = findViewById(R.id.showCountNumber);
        showGoalNumber = findViewById(R.id.showGoalNumber);
        showMessages = findViewById(R.id.textOut);


        //Set에서 받아온 값으로 type과 level과 goalNumber를 설정
        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        goalNumber = intent.getStringExtra("goalNumber");
        level = intent.getStringExtra("level");

        //진동기능 발생기의 핸들을 구해서 멤버변수에 저장
        mVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        //센서관리자 핸들을 구한다.(센서관리자는 new로 생성하지 않고 시스템에서 빌려옴)
        mSensorMgr = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 운동 강도 설정
        setArk(level);

        count = 0;
        //------목표횟수 출력------
        //goal number를 ElbowSet에서 받아와서 int로 바꿈
        goal = Integer.parseInt(goalNumber);
        // 예외처리: xml에서 자연수만 입력받게 설정

        //받아온 goal 를 string형태로 showGoalNumber에서 출력
        showGoalNumber.setText(Integer.toString(goal));

        //-----------센서 동작------------
        startSensor();

        //-----------스레드이용하여 카운트----------
        isThread = true;
        thread = new Thread() {
            public void run() {
                try {
                    boolean minmaxflag = false;
                    boolean firstCheck = true;
                    sleep(100); // 센서가 최초에 0부터 시작하므로 처음부터 Gx<min 에서 카운트 되는 걸 막기 위해 0.1초의 딜레이를 줌
                    handler.sendEmptyMessage(0);
                    while (firstCheck) {
                        sleep(100);
                        warningVibrate();
                        // 예외 각도가 8초 이상 지속되면 정정 음성 출력
                        if (wrongAngleCount == 80) {
                            wrongAngleCount = 0;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null, null);
                            else
                                tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null);
                        }
                        // 올바른 각도로 돌아올 시 음성 조기 종료
                        if (Gy < 15 && Gy > -15 && Gz >= 0) {
                            tts.stop();
                        }
                        if (Gx < minArk && Gy < 15 && Gy > -15 && Gz >= 0) {
                            count = 1;
                            mVib.vibrate(300); // 진동
                            handler.sendEmptyMessage(1);
                            minmaxflag = true;      // 다음에 체크할 숫자 확인
                            firstCheck = false;
                        } else if (Gx > maxArk && Gy < 15 && Gy > -15 && Gz >= 0) {
                            count = 1;
                            mVib.vibrate(300); // 진동
                            handler.sendEmptyMessage(0);
                            minmaxflag = false;     // 다음에 체크할 숫자 확인
                            firstCheck = false;
                        }
                    }
                    while (count < 2 * goal) {
                        int countCheck = count + 1;
                        if (minmaxflag == true) {
                            while (count < countCheck) {
                                sleep(100);
                                warningVibrate();
                                if (wrongAngleCount == 80) {
                                    wrongAngleCount = 0;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                        tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null, null);
                                    else
                                        tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null);
                                }
                                if (Gy < 15 && Gy > -15 && Gz >= 0) {
                                    tts.stop();
                                }
                                if (Gx > maxArk && Gy < 15 && Gy > -15 && Gz >= 0) {
                                    mVib.vibrate(300); // 진동
                                    count++;
                                }
                            }
                            handler.sendEmptyMessage(0);
                            minmaxflag = false;
                        } else {
                            while (count < countCheck) {
                                sleep(100);
                                warningVibrate();
                                if (wrongAngleCount == 80) {
                                    wrongAngleCount = 0;
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                        tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null, null);
                                    else
                                        tts.speak(getString(R.string.wrongText), TextToSpeech.QUEUE_FLUSH, null);
                                }
                                if (Gy < 15 && Gy > -15 && Gz >= 0) {
                                    tts.stop();
                                }
                                if (Gx < minArk && Gy < 15 && Gy > -15 && Gz >= 0) {
                                    mVib.vibrate(300); // 진동
                                    count++;
                                }
                            }
                            handler.sendEmptyMessage(1);
                            minmaxflag = true;
                        }
                    }

                    //성공 알림 진동
                    sleep(500);
                    if (count == goal * 2) {
                        mVib.vibrate(1500);
                        sleep(1600);
                        mVib.vibrate(1500);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // DB에 운동기록 입력
                insertDB(type, level, goal);

                // 다음 액티비티 ElbowFinish 로 이동 (type과 goal 전송)
                Intent intent = new Intent(ElbowCounting.this, Finish.class);
                intent.putExtra("type", type);
                intent.putExtra("goalNumber", goalNumber);
                startActivity(intent);
                finish();
            }
        };
        thread.start();
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                showMessages.setText(R.string.armdown);
                showCountNumber.setText(String.valueOf(count / 2));
            } else if (msg.what == 1) {
                showMessages.setText(R.string.armup);
                showCountNumber.setText(String.valueOf(count / 2));
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        String strMsg = "";
        float v[] = event.values;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                Gx = Math.toDegrees(atan(v[0] / Math.sqrt(v[1] * v[1] + v[2] * v[2])));
                Gy = Math.toDegrees(atan(v[1] / Math.sqrt(v[0] * v[0] + v[2] * v[2])));
                Gz = Math.toDegrees(atan(v[2] / Math.sqrt(v[0] * v[0] + v[1] * v[1])));
                break;
            //자이로 센서 이벤트 일 때
            case Sensor.TYPE_GYROSCOPE:
//                double roll = roll + v[0];
//                double pitch = pitch + v[1];
//                double yaw = yaw + v[2];
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 뒤로가기 방지
    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    //-----------센서 동작------------
    public void startSensor() {
        //가속도 센서 이벤트를 시작
        Sensor sensorAcceler = mSensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        //액티비티클래스가 센서 이벤트 리스너를 상속
        if (sensorAcceler != null)
            mSensorMgr.registerListener(this, sensorAcceler, SensorManager.SENSOR_DELAY_UI);

        //자이로 센서 이벤트를 시작
        Sensor sensorGyro = mSensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        //액티비티클래스가 센서 이벤트 리스너를 상속
        if (sensorGyro != null)
            mSensorMgr.registerListener(this, sensorGyro, SensorManager.SENSOR_DELAY_UI);
    }

    // level에 따라 minArk, maxArk 변경
    public void setArk(String level) {
        switch (level) {
            case "Lv 1":
                maxArk = 60;
                minArk = 40;
                break;
            case "Lv 2":
                maxArk = 70;
                minArk = 30;
                break;
            case "Lv 3":
                maxArk = 80;
                minArk = 20;
                break;
            case "Lv 4":
                maxArk = 80;
                minArk = 10;
                break;
            case "Lv 5":
                maxArk = 85;
                minArk = 00;
                break;
            default:
                maxArk = 80;
                minArk = 20;
        }
    }

    // y축 기울기에 따라 바르지 않은 자세 경고 진동출력(빠르고 약한 진동)
    // 기능 실행시 항상 유지되도록 해야함
    public void warningVibrate() {
        if (Gy < -15 || Gy > 15 || Gz < 0) {
            wrongAngleCount++;
            mVib.vibrate(1);
        }
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
        type = getString(R.string.elbow);
        mDbOpenHelper.insertColumn(dateTime, type, level, goal);

        return flag;
    }
}