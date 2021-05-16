package com.example.rehabili_sample1.ui.arm.wrist;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.Set;
import com.example.rehabili_sample1.ui.TTSReader;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class WristActivity extends AppCompatActivity {
    private TextView exerciseType;
    private String type;
    private WebView webView;
    private String url = "https://www.youtube.com/embed/db7IF7viI_o";
    String text;
    TTSReader ttsReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrist);

        exerciseType = (TextView) findViewById(R.id.wristText);
        type = exerciseType.getText().toString();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClientClass());
        FloatingActionButton wristNext = (FloatingActionButton)findViewById(R.id.nextWrist);
        wristNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // type 값을 가지고 다음 액티비티 Set으로 이동
                ttsReader.ttsStop();
                Intent intent = new Intent(WristActivity.this, Set.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });
        //tts를

        TextView textView = (TextView)findViewById(R.id.wrist_explain);
        text = getString(R.string.wrist_text);
        ttsReader = new TTSReader();
        Locale systemLocale = getResources().getConfiguration().locale;

        ttsReader.setTTSReader(this,textView,text,systemLocale);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        ttsReader.ttsRemove();
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