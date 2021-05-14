package com.example.rehabili_sample1.ui;
import android.content.Context;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rehabili_sample1.R;

import java.util.Locale;

public class TTSReader extends AppCompatActivity{
    private TextToSpeech tts;
    int sw = 0;
    TextView view;
    public void setTTSReader(Context context, TextView textView, String text, Locale systemLocale ){
        tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    //사용할 언어를 설정
                    Locale systemLocale1 = systemLocale;
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
        view = textView;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sw==0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
                    else
                        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
                    sw=1;
                }else {
                    tts.stop();
                    sw=0;
                }
            }
        });

    }

    public void ttsRemove(){
        if(tts != null){
            tts.stop();
            tts.shutdown();
            tts = null;
        }
    }
}
