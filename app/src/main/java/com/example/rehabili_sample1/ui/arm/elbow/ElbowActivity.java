package com.example.rehabili_sample1.ui.arm.elbow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.Set;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class ElbowActivity extends AppCompatActivity {
    private TextView exerciseType;
    private String type;
    private WebView webView;
    private String url = "https://www.youtube.com/embed/iA5K3X29lrA";
    private TextToSpeech textToSpeech;
    String text;
    int sw = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elbow);

        exerciseType = (TextView) findViewById(R.id.elbowText);
        type = exerciseType.getText().toString();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //사용할 언어를 설정
                    Locale systemLocale = getResources().getConfiguration().locale;
                    int result = textToSpeech.setLanguage(systemLocale);

                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {

                    } else {
                        //음성 톤
                        textToSpeech.setPitch(1);
                        //읽는 속도
                        textToSpeech.setSpeechRate(1);

                    }
                }
            }
        });
        TextView textView = (TextView)findViewById(R.id.elbow_explain);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw==0) {
                    Speech();
                    sw=1;
                }else {
                    textToSpeech.stop();
                    sw=0;
                }
            }
        });

        FloatingActionButton elbowNext = (FloatingActionButton)findViewById(R.id.nextElbow);
        elbowNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // type 값을 가지고 다음 액티비티 Set으로 이동
                Intent intent = new Intent(ElbowActivity.this, Set.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

    }


    private void Speech() {
        // QUEUE_FLUSH: Queue 값을 초기화한 후 값을 넣는다.
        // QUEUE_ADD: 현재 Queue에 값을 추가하는 옵션이다.
        // API 21
        text = getString(R.string.elbow_text);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)

            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
            // API 20
        else
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            textToSpeech = null;
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private class WebViewClientClass extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {//현재페이지의 URL을 읽음
            view.loadUrl(url);
            return true;
        }
    }
}