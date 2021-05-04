package com.example.rehabili_sample1.ui.arm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowActivity;

public class ArmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm);

        Button wristButton = (Button)findViewById(R.id.wrist);
        Button elbowButton = (Button)findViewById(R.id.elbow);
        String non = "미구현 항목입니다.";
        wristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ArmActivity.this, non , android.widget.Toast.LENGTH_SHORT).show();

            }
        });
        elbowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArmActivity.this, ElbowActivity.class);
                startActivity(intent);
            }
        });
    }
}