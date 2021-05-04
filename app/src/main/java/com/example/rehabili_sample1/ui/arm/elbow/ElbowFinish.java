package com.example.rehabili_sample1.ui.arm.elbow;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rehabili_sample1.MainActivity;
import com.example.rehabili_sample1.R;
import com.example.rehabili_sample1.ui.arm.ArmActivity;

public class ElbowFinish extends AppCompatActivity {

    // ArmActivity에서 받아옴
    private TextView exerciseType;
    private TextView goalNumber1, goalNumber2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_elbow_finish);

        exerciseType = findViewById(R.id.exerciseType);
        goalNumber1 = findViewById(R.id.goalNumber1);
        goalNumber2 = findViewById(R.id.goalNumber2);

        Intent intent = getIntent();
        exerciseType.setText("팔꿈치 구부렸다 펴기");
        String goalNumber1 = intent.getStringExtra("goalNumber");
        String goalNumber2 = intent.getStringExtra("goalNumber");

        Button restartButton = (Button)findViewById(R.id.restartButton);
        Button homeButton = (Button)findViewById(R.id.homeButton);

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElbowFinish.this, ElbowSet.class);
                startActivity(intent);
            }
        });

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ElbowFinish.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}