package com.example.rehabili_sample1.ui.arm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.elbow.ElbowActivity;
import com.example.rehabili_sample1.ui.arm.grip.GripActivity;
import com.example.rehabili_sample1.ui.arm.wrist.WristActivity;

public class ArmActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arm);

        Button wristButton = (Button) findViewById(R.id.wrist);
        Button gripButton = (Button) findViewById(R.id.grip);
        Button elbowButton = (Button) findViewById(R.id.elbow);
        String non = "미구현 항목입니다.";
        wristButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArmActivity.this, WristActivity.class);
                startActivity(intent);
            }
        });
        gripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ArmActivity.this, GripActivity.class);
                startActivity(intent);
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