package com.example.rehabili_sample1.ui.arm.grip;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.Set;
import com.example.rehabili_sample1.ui.TTSReader;
import com.example.rehabili_sample1.ui.arm.wrist.WristActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Locale;

public class GripActivity extends AppCompatActivity {
    private TextView exerciseType;
    private String type;
    private WebView webView;
    private String url = "https://www.youtube.com/embed/6nLLIVUFchA";
    String text;
    TTSReader ttsReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grip);

        exerciseType = (TextView) findViewById(R.id.gripText);
        type = exerciseType.getText().toString();

        webView = (WebView) findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new GripActivity.WebViewClientClass());
        Button gripNext = (Button) findViewById(R.id.nextGrip);
        gripNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // type 값을 가지고 다음 액티비티 gripCounting으로 이동
                Intent intent = new Intent(GripActivity.this, GripCounting.class);
                intent.putExtra("type", type);
                startActivity(intent);
            }
        });

        TextView textView = (TextView)findViewById(R.id.grip_explain);
        text = getString(R.string.grip_text);
        ttsReader = new TTSReader();
        Locale systemLocale = getResources().getConfiguration().locale;

        ttsReader.setTTSReader(this,textView,text,systemLocale);

    }
    @Override
    protected void onStop() {
        super.onStop();
        // TTS 객체가 남아있다면 실행을 중지하고 메모리에서 제거한다.
        ttsReader.ttsRemove();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
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