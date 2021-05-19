package com.rehabili.rehabili_sample1.ui.arm.wrist;

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

import com.rehabili.rehabili_sample1.DbOpenHelper;
import com.rehabili.rehabili_sample1.R;
import com.rehabili.rehabili_sample1.ui.Finish;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static java.lang.Math.atan;

public class WristCountingRight extends AppCompatActivity implements SensorEventListener {
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

    private double maxArk = -45;
    private double minArk = -45;

    //--------------- 자이로 변수-------------------
    private double pitch = 0;
    private double roll = 0;
    private double yaw = 0;
    private double rollDegree;

    //timestamp and dt
    private double timestamp;
    private double dt;

    private static final float NS2S = 1.0f / 1000000000.0f;
    //---------------------------------------------

    boolean isThread = false;
    Thread thread;

    // 음성출력
    int wrongAngleCount = 0;
    private TextToSpeech tts;
    private TextToSpeech tts2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrist_counting_right);

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

        // tts
        tts2 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
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
        //goal number를 wristSet에서 받아와서 int로 바꿈
        goal = Integer.parseInt(goalNumber);
        // 예외처리 해줘야함, 입력이 없거나, 문자가 입력되면 앱이 강제종료됨.
        // -> onClick눌리기 전에 확인하고 toast로 "잘못된 입력입니다." 띄워야함.
        // xml에서 자연수만 입력받게 설정함

        //받아온 goal 를 string형태로 showGoalNumber에서 출력
        showGoalNumber.setText(Integer.toString(goal));

        //-----------센서 동작------------
        startSensor();

        //-----------스레드이용하여 카운트----------
        isThread = true;
        thread = new Thread() {
            public void run() {
                try {
                    boolean check = true;
                    count = 0;
                    sleep(100); // 센서가 최초에 0부터 시작하므로 처음부터 Gx<min 에서 카운트 되는 걸 막기 위해 0.1초의 딜레이를 줌
                    handler.sendEmptyMessage(1);
                    while (count < 2 * goal) {
                        check = true;
                        while (check) {
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
                            if (Gx < -20 && Gx > -70 && Gy > 0) {
                                wrongAngleCount = 0;
                                tts.stop();
                            }
                            if (Gz < 10 && Gz > -10 && Gx > -70 && Gx < -20 && Gy > 0) {
                                roll = yaw = pitch = 0;
                                count++;
                                mVib.vibrate(300); // 진동
                                handler.sendEmptyMessage(0); // 카운트 출력
                                check = false;
                            }
                        }
                        check = true;
                        while (check) {
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
                            if (Gx < 70 && Gx > 20 && Gy > 0) {
                                wrongAngleCount = 0;
                                tts.stop();
                            }
                            // Gz에 따른 roll 값 보정(드리프트현상이 일정 각도를 넘어가 카운트 안되는 현상이 발생시 Gz에 따라 roll을 0으로 수정)
                            if (Gz < 8 && Gz > -8) {
                                roll = 0;
                            }
                            // 정상동작시 진동 출력후 카운트
                            if (rollDegree < minArk && Gx > -70 && Gx < -20 && Gy > 0) {
                                count++;
                                mVib.vibrate(300); // 진동
                                handler.sendEmptyMessage(1); // 카운트 출력
                                check = false;
                            }
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

                // 다음 액티비티 Finish 로 이동 (type과 goal 전송)
                Intent intent = new Intent(WristCountingRight.this, Finish.class);
                intent.putExtra("type", type);
                intent.putExtra("goalNumber", goalNumber);
                startActivity(intent);
            }
        };

        thread.start();

    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0) {
                showMessages.setText(R.string.wristdown);
                showCountNumber.setText(String.valueOf(count / 2));
                tts2.speak(getString(R.string.wristdown), TextToSpeech.QUEUE_FLUSH, null);
            } else if (msg.what == 1) {
                showMessages.setText(R.string.wristup);
                showCountNumber.setText(String.valueOf(count / 2));
                tts2.speak(getString(R.string.wristup), TextToSpeech.QUEUE_FLUSH, null);
            }
        }
    };

    @Override
    public void onSensorChanged(SensorEvent event) {
        String strMsg = "";
        float v[] = event.values;

        switch (event.sensor.getType()) {
            //가속도 센서 이벤트 일 때 X,Y,Z축 가속도값과 X,Y축의 기울기를 화면에 표시.
            //나중에 삭제
            case Sensor.TYPE_ACCELEROMETER:
                Gx = Math.toDegrees(atan(v[0] / Math.sqrt(v[1] * v[1] + v[2] * v[2])));
                Gy = Math.toDegrees(atan(v[1] / Math.sqrt(v[0] * v[0] + v[2] * v[2])));
                Gz = Math.toDegrees(atan(v[2] / Math.sqrt(v[0] * v[0] + v[1] * v[1])));
                break;

            //자이로 센서 이벤트 일 때
            case Sensor.TYPE_GYROSCOPE:
                // 각속도를 적분하여 회전각을 추출하기 위해 적분 간격(dt)를 구함
                // dt : 센서가 현재 상태를 감지하는 시간간격
                // NS2S : nano sec를 sec로
                dt = (event.timestamp - timestamp) * NS2S;
                timestamp = event.timestamp;

                // 센서를 활성화 하였을 때 처음 timestamp가 0일 때에는 dt값이 올바르지 않으므로 넘어감.
                if (dt - timestamp * NS2S != 0) {

                    roll = roll + v[0] * dt;
                    pitch = pitch + v[1] * dt;
                    yaw = yaw + v[2] * dt;

                    rollDegree = Math.toDegrees(roll);
                }
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
                maxArk = 40;
                minArk = -40;
                break;
            case "Lv 2":
                maxArk = 50;
                minArk = -50;
                break;
            case "Lv 3":
                maxArk = 60;
                minArk = -60;
                break;
            case "Lv 4":
                maxArk = 70;
                minArk = -70;
                break;
            case "Lv 5":
                maxArk = 80;
                minArk = -80;
                break;
            default:
                maxArk = 45;
                minArk = -45;
        }
    }

    // y축 기울기에 따라 바르지 않은 자세 경고 진동출력(빠르고 약한 진동)
    // 기능 실행시 항상 유지되도록 해야함
    public void warningVibrate() {
        if (Gx < -70 || Gx > -20 || Gy < 0) {
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
        // DB정렬을 위해 문자열 변환 후 저장
        type = getString(R.string.wrist) + "          ";
        mDbOpenHelper.insertColumn(dateTime, type, level, goal);

        return flag;
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
}